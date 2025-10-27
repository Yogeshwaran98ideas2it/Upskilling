package com.upskilling.experiment.entity;

import com.upskilling.experiment.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor; // Added for JPA compliance and Lombok generation
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * User entity representing system users in the task management system.
 * Supports role-based access control with ADMIN, MANAGER, and MEMBER roles.
 * Has bidirectional relationships with projects, tasks, comments, and activity logs.
 * Uses Lombok for boilerplate code generation (getters, setters, equals, hashCode).
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "app_user") // Avoids conflict with 'user' reserved keyword in some DBs
@Data
@NoArgsConstructor // Required for JPA/Hibernate
@EqualsAndHashCode(exclude = {"projects", "assignedTasks", "createdTasks", "comments", "activityLogs"})
@ToString(exclude = {"projects", "assignedTasks", "createdTasks", "comments", "activityLogs"})
public class User {
    /** Unique identifier for the user */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User's unique username for login */
    @Column(nullable = false, unique = true)
    private String username;

    /** User's email address (unique) */
    @Column(nullable = false, unique = true)
    private String email;

    /** Hashed password using BCrypt (never store plain text passwords) */
    @Column(nullable = false)
    private String password;

    /** User's role for access control (ADMIN, MANAGER, or MEMBER) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.MEMBER;

    /** Projects where this user is a member */
    @ManyToMany(mappedBy = "members")
    private Set<Project> projects = new HashSet<>();

    /** Tasks created by this user */
    @OneToMany(mappedBy = "creator")
    private Set<Task> createdTasks = new HashSet<>();

    /** Tasks currently assigned to this user */
    @OneToMany(mappedBy = "assignee")
    private Set<Task> assignedTasks = new HashSet<>();

    /** Comments made by this user on tasks */
    @OneToMany(mappedBy = "user")
    private Set<Comment> comments = new HashSet<>();

    /** Activity logs where this user performed actions */
    @OneToMany(mappedBy = "user")
    private Set<ActivityLog> activityLogs = new HashSet<>();

    /**
     * Convenience Constructor for DataLoader/testing purposes.
     * Sets a placeholder email since it is a non-nullable field.
     */
    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = username + "@projectapp.com";
    }
}