package com.upskilling.experiment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * TaskList entity representing Kanban board columns.
 * Each project has multiple task lists (e.g., "To Do", "In Progress", "Done").
 * Tasks are organized within these lists to represent workflow stages.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"tasks"})
@ToString(exclude = {"tasks"})
public class TaskList {
    /** Unique identifier for the task list */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Title of the task list (e.g., "To Do", "In Progress") */
    @Column(nullable = false, length = 100)
    private String title;

    /** Project this task list belongs to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    /** Tasks within this task list (ordered by ID ascending) */
    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private Set<Task> tasks = new HashSet<>();
}