package com.upskilling.experiment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Tag entity representing categorization labels for tasks.
 * Tags are reusable across multiple tasks and provide flexible organization.
 * Case-insensitive unique constraint ensures no duplicate tag names.
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
public class Tag {
    /** Unique identifier for the tag */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tag name (required, unique, max 50 characters) */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /** Tasks associated with this tag */
    @ManyToMany(mappedBy = "tags")
    private Set<Task> tasks = new HashSet<>();

    /**
     * Constructor for creating a tag with a name
     * 
     * @param name The tag name
     */
    public Tag(String name) {
        this.name = name;
    }
}