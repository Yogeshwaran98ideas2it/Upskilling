/**
 * Repository interface for database operations on ActivityLog entities.
 * Extends JpaRepository to provide CRUD operations.
 * Includes custom query method for retrieving activity logs by task ID.
 * Activity logs provide audit trails and system history.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
package com.upskilling.experiment.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.upskilling.experiment.entity.ActivityLog;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    /**
     * Find all activity logs for a specific task, ordered by timestamp descending
     * 
     * @param taskId The unique identifier of the task
     * @return List of activity logs for the task, ordered by timestamp (newest first)
     */
    List<ActivityLog> findByTaskIdOrderByTimestampDesc(Long taskId);
}