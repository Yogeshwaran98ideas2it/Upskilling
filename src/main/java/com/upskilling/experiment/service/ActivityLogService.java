package com.upskilling.experiment.service;

import java.util.List;

import com.upskilling.experiment.dto.response.ActivityLogResponseDTO;
import com.upskilling.experiment.entity.Project;
import com.upskilling.experiment.entity.Task;
import com.upskilling.experiment.entity.User;

/**
 * Service interface for activity log management.
 * Defines methods for creating and retrieving activity logs for audit trails.
 * Provides pagination support for retrieving recent activities.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public interface ActivityLogService {

    /**
     * Create a new activity log entry
     * 
     * @param type Type of activity (e.g., TASK_CREATED, STATUS_CHANGED)
     * @param details Detailed description of the activity
     * @param user User who performed the action
     * @param task Task related to the activity (optional)
     * @param project Project related to the activity (optional)
     * @return ActivityLogResponseDTO with created log details
     */
    ActivityLogResponseDTO createLog(String type, String details, User user, Task task, Project project);
    
    /**
     * Get recent activity logs with pagination
     * 
     * @param limit Maximum number of logs to retrieve
     * @return List of recent activity logs ordered by timestamp descending
     */
    List<ActivityLogResponseDTO> getRecentLogs(int limit);
}
