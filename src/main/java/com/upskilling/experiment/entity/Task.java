package com.upskilling.experiment.entity;

import com.upskilling.experiment.enums.TaskPriority;
import com.upskilling.experiment.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Task entity representing work items in the task management system.
 * Tasks belong to a task list (Kanban column) within a project.
 * Supports priority levels, due dates, status tracking, comments, tags, and attachments.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"comments", "tags", "activityLogs"})
@ToString(exclude = {"comments", "tags", "activityLogs"})
public class Task {
    /** Unique identifier for the task */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Task title (required, max 150 characters) */
    @Column(nullable = false, length = 150)
    private String title;

    /** Task description (optional, max 1000 characters) */
    @Column(length = 1000)
    private String description;

    /** Current status of the task (TO_DO, IN_PROGRESS, REVIEW, DONE, BLOCKED) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TO_DO;

    /** Priority level of the task (LOW, MEDIUM, HIGH, CRITICAL) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority = TaskPriority.MEDIUM;

    /** Timestamp when task was created (auto-generated) */
    @CreationTimestamp
    private LocalDateTime createdAt;

    /** Optional due date for the task */
    private LocalDate dueDate;

    /** The task list (Kanban column) this task belongs to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_list_id", nullable = false)
    private TaskList taskList;

    /** User assigned to work on this task (optional) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    /** User who created this task */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    /** Tags associated with this task for categorization */
    @ManyToMany
    @JoinTable(
            name = "task_tag",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    /** Comments made on this task */
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    /** Activity logs tracking changes to this task */
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private Set<ActivityLog> activityLogs = new HashSet<>();
}