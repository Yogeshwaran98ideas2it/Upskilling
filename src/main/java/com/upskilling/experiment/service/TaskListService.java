package com.upskilling.experiment.service;

import java.util.List;

import com.upskilling.experiment.dto.request.TaskListRequestDTO;
import com.upskilling.experiment.dto.response.TaskListResponseDTO;

/**
 * Service interface for task list management operations.
 * Defines methods for CRUD operations on task lists (Kanban board columns).
 * Task lists belong to projects and organize tasks into workflow stages.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public interface TaskListService {
    /**
     * Create a new task list for a project
     * 
     * @param request DTO containing task list details
     * @return TaskListResponseDTO with created task list details
     */
    TaskListResponseDTO createTaskList(TaskListRequestDTO request);
    
    /**
     * Get a task list by its unique identifier
     * 
     * @param id The unique identifier of the task list
     * @return TaskListResponseDTO with task list details
     */
    TaskListResponseDTO getTaskListById(Long id);
    
    /**
     * Get all task lists for a specific project
     * 
     * @param projectId The unique identifier of the project
     * @return List of task list response DTOs
     */
    List<TaskListResponseDTO> getListsByProjectId(Long projectId);
    
    /**
     * Update an existing task list
     * 
     * @param id The unique identifier of the task list to update
     * @param request DTO containing updated task list details
     * @return TaskListResponseDTO with updated task list details
     */
    TaskListResponseDTO updateTaskList(Long id, TaskListRequestDTO request);
    
    /**
     * Delete a task list by its unique identifier
     * 
     * @param id The unique identifier of the task list to delete
     */
    void deleteTaskList(Long id);
}