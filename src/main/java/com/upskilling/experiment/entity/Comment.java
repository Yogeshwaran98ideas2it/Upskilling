package com.upskilling.experiment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Comment entity representing user comments on tasks.
 * Comments are linked to both a task and the user who created them.
 * Provides collaboration and communication within the task management system.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@NoArgsConstructor
public class Comment {
    /** Unique identifier for the comment */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Comment content (required, TEXT field for unlimited length) */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /** Timestamp when comment was created (auto-generated) */
    @CreationTimestamp
    private LocalDateTime timestamp;

    /** User who wrote the comment */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Task this comment belongs to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}