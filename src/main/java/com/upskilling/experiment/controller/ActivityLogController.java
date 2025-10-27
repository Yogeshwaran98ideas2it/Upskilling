package com.upskilling.experiment.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upskilling.experiment.dto.response.ActivityLogResponseDTO;
import com.upskilling.experiment.service.ActivityLogService;

/**
 * REST Controller for activity log management operations.
 * Provides endpoints for viewing system activity history and audit logs.
 * Activity logs track all user actions in the system (task creation, updates, assignments, etc.).
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class ActivityLogController {

    /** Service for activity log business logic operations */
    private final ActivityLogService activityLogService;

    /**
     * Retrieve recent activity logs from the system
     * Returns the most recent activities across all projects
     * Useful for displaying activity feeds and audit trails
     * 
     * @param limit Maximum number of logs to retrieve (default: 20, optional)
     * @return ResponseEntity with list of recent activity logs (HTTP 200)
     * 
     * @apiNote Supported activity types include:
     *          - TASK_CREATED: Task was created
     *          - TASK_ASSIGNED: Task was assigned to a user
     *          - TASK_STATUS_CHANGED: Task status was updated
     *          - TASK_COMMENTED: Comment was added to a task
     *          - PROJECT_CREATED: Project was created
     *          - ATTACHMENT_ADDED: File was uploaded
     */
    @GetMapping("/recent")
    public ResponseEntity<List<ActivityLogResponseDTO>> getRecentActivityLogs(@RequestParam(defaultValue = "20") int limit) {
        List<ActivityLogResponseDTO> logs = activityLogService.getRecentLogs(limit);
        return ResponseEntity.ok(logs);
    }
}
