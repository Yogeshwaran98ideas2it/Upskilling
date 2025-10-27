package com.upskilling.experiment.service.impl;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.upskilling.experiment.dto.request.CommentRequestDTO;
import com.upskilling.experiment.dto.request.TaskCreateRequestDTO;
import com.upskilling.experiment.dto.request.TaskUpdateRequestDTO;
import com.upskilling.experiment.dto.response.CommentResponseDTO;
import com.upskilling.experiment.dto.response.TaskResponseDTO;
import com.upskilling.experiment.entity.Comment;
import com.upskilling.experiment.entity.Task;
import com.upskilling.experiment.entity.TaskList;
import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.enums.TaskStatus;
import com.upskilling.experiment.enums.TaskType;
import com.upskilling.experiment.enums.TicketType;
import com.upskilling.experiment.mapper.CommentMapper;
import com.upskilling.experiment.mapper.TaskMapper;
import com.upskilling.experiment.repository.CommentRepository;
import com.upskilling.experiment.repository.ProjectRepository;
import com.upskilling.experiment.repository.TaskListRepository;
import com.upskilling.experiment.repository.TaskRepository;
import com.upskilling.experiment.repository.UserRepository;
import com.upskilling.experiment.service.ActivityLogService;
import com.upskilling.experiment.service.NotificationService;
import com.upskilling.experiment.service.TagService;
import com.upskilling.experiment.service.TaskService;

/**
 * Service implementation for task management business logic.
 * Handles CRUD operations for tasks, task comments, email notifications, and activity logging.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    /** Repository for database operations on Task entities */
    private final TaskRepository taskRepository;
    
    /** Repository for database operations on TaskList entities */
    private final TaskListRepository taskListRepository;
    
    /** Repository for database operations on User entities */
    private final UserRepository userRepository;
    
    /** Repository for database operations on Comment entities */
    private final CommentRepository commentRepository;

    /** Mapper for converting between Task entities and DTOs */
    private final TaskMapper taskMapper;
    
    /** Mapper for converting between Comment entities and DTOs */
    private final CommentMapper commentMapper;

    /** Service for tag management operations */
    private final TagService tagService;
    
    /** Service for activity log operations */
    private final ActivityLogService activityLogService;
    
    /** Service for sending email notifications */
    private final NotificationService notificationService;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, TaskListRepository listRepository, ActivityLogServiceImpl logService, NotificationService notificationService, ProjectRepository projectRepository, TaskListRepository taskListRepository, CommentRepository commentRepository, TaskMapper taskMapper, CommentMapper commentMapper, TagService tagService, ActivityLogService activityLogService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskListRepository = taskListRepository;
        this.commentRepository = commentRepository;
        this.taskMapper = taskMapper;
        this.commentMapper = commentMapper;
        this.tagService = tagService;
        this.activityLogService = activityLogService;
        this.notificationService = notificationService;

    }

    /**
     * Create a new task with the provided details
     * Handles assignee assignment, tag creation, email notifications, and activity logging
     * 
     * @param request DTO containing task creation details
     * @param creator The user creating the task
     * @return TaskResponseDTO with created task details
     * @throws EntityNotFoundException if task list or assignee not found
     */
    @Override
    public TaskResponseDTO createTask(TaskCreateRequestDTO request, User creator) {
        // Validate and fetch the target task list
        TaskList taskList = taskListRepository.findById(request.getTaskListId())
                .orElseThrow(() -> new EntityNotFoundException("Task List not found with ID: " + request.getTaskListId()));

        // Convert DTO to entity and set relationships
        Task task = taskMapper.toEntity(request);
        task.setTaskList(taskList);
        task.setCreator(creator);

        // Handle task assignee if provided
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found."));
            task.setAssignee(assignee);
            // Send email notification to the assignee
            notificationService.notifyTaskAssignment(assignee, task);
        }

        // Process and attach tags to the task
        task.setTags(tagService.findOrCreateTags(request.getTags()));

        // Persist the task to the database
        Task savedTask = taskRepository.save(task);

        // Log the task creation activity
        activityLogService.createLog("TASK_CREATED",
                "Task '" + savedTask.getTitle() + "' created in list '" + taskList.getTitle() + "'.",
                creator, savedTask, taskList.getProject());

        return taskMapper.toDto(savedTask);
    }

    @Override
    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + id));
        return taskMapper.toDto(task);
    }

    /**
     * Update an existing task with new details
     * Handles status changes, assignee reassignment, tag updates, email notifications, and activity logging
     * 
     * @param id The unique identifier of the task to update
     * @param request DTO containing updated task details
     * @param updater The user performing the update
     * @return TaskResponseDTO with updated task details
     * @throws EntityNotFoundException if task or new assignee not found
     */
    @Override
    public TaskResponseDTO updateTask(Long id, TaskUpdateRequestDTO request, User updater) {
        // Fetch the existing task from the database
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + id));

        // Handle task status change if provided
        if (request.getStatus() != null && task.getStatus() != request.getStatus()) {
            String oldStatus = task.getStatus().name();
            task.setStatus(request.getStatus());
            
            // Send email notification to task assignee about status change
            notificationService.notifyTaskStatusChange(task, oldStatus, task.getStatus().name());
            
            // Log the status change activity
            activityLogService.createLog("TASK_STATUS_CHANGED",
                    String.format("Status changed from %s to %s.", oldStatus, task.getStatus().name()),
                    updater, task, task.getTaskList().getProject());
        }

        // Handle assignee reassignment if provided
        if (request.getAssigneeId() != null) {
            Long newAssigneeId = request.getAssigneeId();
            User currentAssignee = task.getAssignee();

            // Only update if assignee is actually changing
            if (currentAssignee == null || !currentAssignee.getId().equals(newAssigneeId)) {
                // Validate and fetch the new assignee
                User newAssignee = userRepository.findById(newAssigneeId)
                        .orElseThrow(() -> new EntityNotFoundException("New assignee not found."));

                // Track the old assignee name for logging
                String oldAssigneeName = currentAssignee != null ? currentAssignee.getUsername() : "Unassigned";
                
                // Update the assignee
                task.setAssignee(newAssignee);
                
                // Send email notification to the new assignee
                notificationService.notifyTaskAssignment(newAssignee, task);

                // Log the reassignment activity
                activityLogService.createLog("TASK_ASSIGNED",
                        String.format("Reassigned from %s to %s.", oldAssigneeName, newAssignee.getUsername()),
                        updater, task, task.getTaskList().getProject());
            }
        }

        // Update other fields (title, description, priority, dueDate, tags)
        taskMapper.updateEntityFromRequest(request, task);

        // Handle tag updates if provided
        if (request.getTags() != null) {
            task.setTags(tagService.findOrCreateTags(request.getTags()));
        }

        // Save the updated task and return DTO
        return taskMapper.toDto(taskRepository.save(task));
    }
    @Override
    public CommentResponseDTO addCommentToTask(Long taskId, CommentRequestDTO request, User commenter) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setUser(commenter);

        Comment savedComment = commentRepository.save(comment);

        activityLogService.createLog("TASK_COMMENTED",
                "Added a comment to the task.",
                commenter, task, task.getTaskList().getProject());

        return commentMapper.toDto(savedComment);
    }


    @Override
    public List<TaskResponseDTO> getTasksByListId(Long taskListId) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new EntityNotFoundException("Task List not found with ID: " + taskListId));

        return taskList.getTasks().stream()
                .map(taskMapper::toDto)
                .collect(java.util.stream.Collectors.toList());
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new EntityNotFoundException("Task not found");
        }
        taskRepository.deleteById(taskId);
        log.info("Task with ID {} deleted", taskId);
    }


    @Override
    public List<CommentResponseDTO> getCommentsByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        return task.getComments().stream()
                .map(commentMapper::toDto)
                .collect(java.util.stream.Collectors.toList());
    }
}