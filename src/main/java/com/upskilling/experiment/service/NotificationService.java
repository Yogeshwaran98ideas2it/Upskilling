package com.upskilling.experiment.service;

import com.upskilling.experiment.entity.Task;
import com.upskilling.experiment.entity.User;

/**
 * Service interface for email notification functionality.
 * Defines methods for sending notifications to users about task events.
 * Email sending is asynchronous to avoid blocking operations.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public interface NotificationService {

    /**
     * Notify a user when they are assigned to a task
     * 
     * @param assignee The user being assigned
     * @param task The task being assigned
     */
    void notifyTaskAssignment(User assignee, Task task);

    /**
     * Notify a user when task status changes
     * 
     * @param task The task whose status changed
     * @param oldStatus The previous status
     * @param newStatus The new status
     */
    void notifyTaskStatusChange(Task task, String oldStatus, String newStatus);

    /**
     * Send a general notification about a task event
     * 
     * @param task The task related to the event
     * @param eventType Type of event (STATUS_CHANGE, ASSIGNMENT, etc.)
     * @param performingUser User who triggered the event
     */
    void sendNotification(Task task, String eventType, User performingUser);

}
