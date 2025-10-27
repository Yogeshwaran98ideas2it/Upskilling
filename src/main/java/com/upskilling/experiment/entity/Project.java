package com.upskilling.experiment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Project entity representing a project in the task management system.
 * Projects contain multiple task lists (Kanban boards) and team members.
 * Used to organize tasks into logical work streams.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"taskLists", "members", "activityLogs"})
@ToString(exclude = {"taskLists", "members", "activityLogs"})
public class Project {
    /** Unique identifier for the project */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Project title (required, max 100 characters) */
    @Column(nullable = false, length = 100)
    private String title;

    /** Project description (optional, max 500 characters) */
    @Column(length = 500)
    private String description;

    /** Timestamp when project was created (auto-generated) */
    @CreationTimestamp
    private LocalDateTime createdAt;

    /** User who created the project */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    /** Team members of the project (many-to-many relationship) */
    @ManyToMany
    @JoinTable(
            name = "project_member",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    /** Task lists (Kanban board columns) belonging to this project */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskList> taskLists = new HashSet<>();

    /** Activity logs related to this project */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<ActivityLog> activityLogs = new HashSet<>();
}