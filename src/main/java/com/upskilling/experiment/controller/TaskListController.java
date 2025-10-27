package com.upskilling.experiment.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.upskilling.experiment.dto.request.TaskListRequestDTO;
import com.upskilling.experiment.dto.response.TaskListResponseDTO;
import com.upskilling.experiment.service.TaskListService;

/**
 * REST Controller for task list management operations.
 * Handles CRUD operations for task lists (Kanban board columns).
 * Task lists are used to organize tasks within a project (e.g., "To Do", "In Progress", "Done").
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/tasklists")
@RequiredArgsConstructor
public class TaskListController {

    /** Service for task list business logic operations */
    private final TaskListService taskListService;

    /**
     * Create a new task list for a project
     * Task lists represent columns in a Kanban board workflow
     * 
     * @param request DTO containing task list title and project ID
     * @return ResponseEntity with created task list details (HTTP 201)
     */
    @PostMapping
    public ResponseEntity<TaskListResponseDTO> createTaskList(@Valid @RequestBody TaskListRequestDTO request) {
        TaskListResponseDTO createdList = taskListService.createTaskList(request);
        return new ResponseEntity<>(createdList, HttpStatus.CREATED);
    }

    /**
     * Retrieve a specific task list by its unique identifier
     * Returns the task list with all associated tasks
     * 
     * @param id The unique identifier of the task list
     * @return ResponseEntity with task list details including nested tasks (HTTP 200)
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskListResponseDTO> getTaskListById(@PathVariable Long id) {
        TaskListResponseDTO list = taskListService.getTaskListById(id);
        return ResponseEntity.ok(list);
    }

    /**
     * Get all task lists for a specific project
     * Returns a complete Kanban board structure with all columns and tasks
     * Useful for displaying the full project workflow
     * 
     * @param projectId The unique identifier of the project
     * @return ResponseEntity with list of task lists for the project (HTTP 200)
     */
    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<TaskListResponseDTO>> getTaskListsByProjectId(@PathVariable Long projectId) {
        List<TaskListResponseDTO> lists = taskListService.getListsByProjectId(projectId);
        return ResponseEntity.ok(lists);
    }

    /**
     * Update an existing task list
     * Allows modification of the task list title or project association
     * 
     * @param id The unique identifier of the task list to update
     * @param request DTO containing updated task list details
     * @return ResponseEntity with updated task list details (HTTP 200)
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskListResponseDTO> updateTaskList(@PathVariable Long id, @Valid @RequestBody TaskListRequestDTO request) {
        TaskListResponseDTO updatedList = taskListService.updateTaskList(id, request);
        return ResponseEntity.ok(updatedList);
    }

    /**
     * Delete a task list by its unique identifier
     * Note: This will also delete all tasks within the task list
     * 
     * @param id The unique identifier of the task list to delete
     * @return HTTP 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskList(@PathVariable Long id) {
        taskListService.deleteTaskList(id);
    }
}
