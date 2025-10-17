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

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;

    private final TagService tagService;
    private final ActivityLogService activityLogService;
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

    @Override
    public TaskResponseDTO createTask(TaskCreateRequestDTO request, User creator) {
        TaskList taskList = taskListRepository.findById(request.getTaskListId())
                .orElseThrow(() -> new EntityNotFoundException("Task List not found with ID: " + request.getTaskListId()));

        Task task = taskMapper.toEntity(request);
        task.setTaskList(taskList);
        task.setCreator(creator);

        // Handle Assignee
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found."));
            task.setAssignee(assignee);
            notificationService.notifyTaskAssignment(assignee, task);
        }

        // Handle Tags
        task.setTags(tagService.findOrCreateTags(request.getTags()));

        Task savedTask = taskRepository.save(task);

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

    @Override
    public TaskResponseDTO updateTask(Long id, TaskUpdateRequestDTO request, User updater) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + id));

        // --- Log and update status ---
        if (request.getStatus() != null && task.getStatus() != request.getStatus()) {
            String oldStatus = task.getStatus().name();
            task.setStatus(request.getStatus());
            notificationService.notifyTaskStatusChange(task, oldStatus, task.getStatus().name());
            activityLogService.createLog("TASK_STATUS_CHANGED",
                    String.format("Status changed from %s to %s.", oldStatus, task.getStatus().name()),
                    updater, task, task.getTaskList().getProject());
        }

        // --- Log and update assignee ---
        if (request.getAssigneeId() != null) {
            Long newAssigneeId = request.getAssigneeId();
            User currentAssignee = task.getAssignee();

            if (currentAssignee == null || !currentAssignee.getId().equals(newAssigneeId)) {
                User newAssignee = userRepository.findById(newAssigneeId)
                        .orElseThrow(() -> new EntityNotFoundException("New assignee not found."));

                String oldAssigneeName = currentAssignee != null ? currentAssignee.getUsername() : "Unassigned";
                task.setAssignee(newAssignee);
                notificationService.notifyTaskAssignment(newAssignee, task);

                activityLogService.createLog("TASK_ASSIGNED",
                        String.format("Reassigned from %s to %s.", oldAssigneeName, newAssignee.getUsername()),
                        updater, task, task.getTaskList().getProject());
            }
        }

// Map other fields (title, description, priority, dueDate)
        taskMapper.updateEntityFromRequest(request, task);

        // Tags update
        if (request.getTags() != null) {
            task.setTags(tagService.findOrCreateTags(request.getTags()));
        }

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