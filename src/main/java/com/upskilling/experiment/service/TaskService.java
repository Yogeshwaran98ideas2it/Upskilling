package com.upskilling.experiment.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.upskilling.experiment.dto.request.CommentRequestDTO;
import com.upskilling.experiment.dto.request.TaskCreateRequestDTO;
import com.upskilling.experiment.dto.request.TaskUpdateRequestDTO;
import com.upskilling.experiment.dto.response.CommentResponseDTO;
import com.upskilling.experiment.dto.response.TaskResponseDTO;
import com.upskilling.experiment.entity.Task;
import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.enums.TaskStatus;
import com.upskilling.experiment.enums.TaskType;
import com.upskilling.experiment.enums.TicketType;

/**
 * Service interface for task management operations.
 * Defines methods for CRUD operations on tasks, task comments, and status updates.
 * Triggers email notifications for task assignments and status changes.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public interface TaskService {

    /**
     * Create a new task with the provided details
     * 
     * @param request DTO containing task creation details
     * @param creator The user creating the task
     * @return TaskResponseDTO with created task details
     */
    TaskResponseDTO createTask(TaskCreateRequestDTO request, User creator);
    
    /**
     * Retrieve a task by its unique identifier
     * 
     * @param id The unique identifier of the task
     * @return TaskResponseDTO with task details
     */
    TaskResponseDTO getTaskById(Long id);
    
    /**
     * Get all tasks belonging to a specific task list
     * 
     * @param taskListId The unique identifier of the task list
     * @return List of task response DTOs
     */
    List<TaskResponseDTO> getTasksByListId(Long taskListId);
    
    /**
     * Update an existing task
     * 
     * @param id The unique identifier of the task to update
     * @param request DTO containing updated task details
     * @param updater The user performing the update
     * @return TaskResponseDTO with updated task details
     */
    TaskResponseDTO updateTask(Long id, TaskUpdateRequestDTO request, User updater);
    
    /**
     * Delete a task by its unique identifier
     * 
     * @param id The unique identifier of the task to delete
     */
    void deleteTask(Long id);

    /**
     * Add a comment to a specific task
     * 
     * @param taskId The unique identifier of the task
     * @param request DTO containing the comment content
     * @param commenter The user adding the comment
     * @return CommentResponseDTO with created comment details
     */
    CommentResponseDTO addCommentToTask(Long taskId, CommentRequestDTO request, User commenter);
    
    /**
     * Get all comments for a specific task
     * 
     * @param taskId The unique identifier of the task
     * @return List of comment response DTOs
     */
    List<CommentResponseDTO> getCommentsByTaskId(Long taskId);
}
