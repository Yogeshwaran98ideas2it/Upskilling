package com.upskilling.experiment.controller;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.upskilling.experiment.dto.request.CommentRequestDTO;
import com.upskilling.experiment.dto.request.TaskCreateRequestDTO;
import com.upskilling.experiment.dto.request.TaskUpdateRequestDTO;
import com.upskilling.experiment.dto.response.CommentResponseDTO;
import com.upskilling.experiment.dto.response.TaskResponseDTO;
import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.repository.UserRepository;
import com.upskilling.experiment.service.TaskService;

/**
 * REST Controller for task management operations.
 * Handles CRUD operations for tasks and task comments.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    /** Service for task business logic operations */
    private final TaskService taskService;
    
    /** Repository for user data access - Used to simulate current authenticated user */
    private final UserRepository userRepository;

    /**
     * Utility method to fetch the currently authenticated user
     * In a production environment, this would use @AuthenticationPrincipal annotation
     * Currently simulates authentication by fetching the admin user
     * 
     * @return The currently authenticated User entity
     * @throws EntityNotFoundException if the admin user is not found in the database
     */
    private User getCurrentAuthenticatedUser() {
        // TODO: Replace with Spring Security @AuthenticationPrincipal in production
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // Fetch the full User entity from the database to check the role
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found in database."));
    }

    /**
     * Create a new task in the specified task list
     * Automatically triggers email notification if task is assigned to a user
     * 
     * @param request DTO containing task details (title, description, taskListId, priority, etc.)
     * @return ResponseEntity with created task details (HTTP 201)
     * @throws EntityNotFoundException if task list or assignee not found
     */
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskCreateRequestDTO request) {
        User creator = getCurrentAuthenticatedUser();
        TaskResponseDTO createdTask = taskService.createTask(request, creator);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    /**
     * Retrieve a task by its unique identifier
     * Returns complete task information including assignee, creator, tags, and comments
     * 
     * @param id The unique identifier of the task
     * @return ResponseEntity with task details (HTTP 200)
     * @throws EntityNotFoundException if task with given ID doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        TaskResponseDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    /**
     * Get all tasks belonging to a specific task list
     * Useful for Kanban board column views
     * 
     * @param taskListId The unique identifier of the task list
     * @return ResponseEntity with list of tasks in the specified list (HTTP 200)
     * @throws EntityNotFoundException if task list with given ID doesn't exist
     */
    @GetMapping("/by-list/{taskListId}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByTaskListId(@PathVariable Long taskListId) {
        List<TaskResponseDTO> tasks = taskService.getTasksByListId(taskListId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Update an existing task
     * Triggers email notifications on status change or assignee reassignment
     * 
     * @param id The unique identifier of the task to update
     * @param request DTO containing updated task details
     * @return ResponseEntity with updated task details (HTTP 200)
     * @throws EntityNotFoundException if task or new assignee not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskUpdateRequestDTO request) {
        User updater = getCurrentAuthenticatedUser();
        TaskResponseDTO updatedTask = taskService.updateTask(id, request, updater);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Delete a task by its unique identifier
     * Permanently removes the task from the system
     * 
     * @param id The unique identifier of the task to delete
     * @return HTTP 204 No Content on successful deletion
     * @throws EntityNotFoundException if task with given ID doesn't exist
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    /**
     * Add a comment to a specific task
     * Used for team collaboration and task discussions
     * 
     * @param taskId The unique identifier of the task to comment on
     * @param request DTO containing the comment content
     * @return ResponseEntity with created comment details (HTTP 201)
     * @throws EntityNotFoundException if task with given ID doesn't exist
     */
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<CommentResponseDTO> addCommentToTask(@PathVariable Long taskId, @Valid @RequestBody CommentRequestDTO request) {
        User commenter = getCurrentAuthenticatedUser();
        CommentResponseDTO comment = taskService.addCommentToTask(taskId, request, commenter);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    /**
     * Get all comments for a specific task
     * Returns comments ordered by timestamp
     * 
     * @param taskId The unique identifier of the task
     * @return ResponseEntity with list of task comments (HTTP 200)
     * @throws EntityNotFoundException if task with given ID doesn't exist
     */
    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByTaskId(@PathVariable Long taskId) {
        List<CommentResponseDTO> comments = taskService.getCommentsByTaskId(taskId);
        return ResponseEntity.ok(comments);
    }
}
