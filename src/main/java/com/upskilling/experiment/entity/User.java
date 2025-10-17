package com.upskilling.experiment.entity;

import com.upskilling.experiment.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor; // Added for JPA compliance and Lombok generation
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user") // Avoids conflict with 'user' reserved keyword in some DBs
@Data
@NoArgsConstructor // Required for JPA/Hibernate
@EqualsAndHashCode(exclude = {"projects", "assignedTasks", "createdTasks", "comments", "activityLogs"})
@ToString(exclude = {"projects", "assignedTasks", "createdTasks", "comments", "activityLogs"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // Store hashed password

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.MEMBER;

    // Projects where this user is a member
    @ManyToMany(mappedBy = "members")
    private Set<Project> projects = new HashSet<>();

    // Tasks created by this user
    @OneToMany(mappedBy = "creator")
    private Set<Task> createdTasks = new HashSet<>();

    // Tasks assigned to this user
    @OneToMany(mappedBy = "assignee")
    private Set<Task> assignedTasks = new HashSet<>();

    // Comments made by this user
    @OneToMany(mappedBy = "user")
    private Set<Comment> comments = new HashSet<>();

    // Activities logged by this user
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