package com.upskilling.experiment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * ActivityLog entity representing system activity and audit trails.
 * Tracks all user actions in the system (task creation, updates, assignments, etc.).
 * Provides history and accountability for all system operations.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@NoArgsConstructor
public class ActivityLog {
    /** Unique identifier for the activity log */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Type of activity (e.g., TASK_CREATED, STATUS_CHANGED, COMMENT_ADDED) */
    @Column(nullable = false, length = 50)
    private String activityType;

    /** Detailed description of the activity */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String details;

    /** Timestamp when the activity occurred (auto-generated) */
    @CreationTimestamp
    private LocalDateTime timestamp;

    /** User who performed the action (required) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Task related to the activity (optional) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    /** Project related to the activity (optional) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
}