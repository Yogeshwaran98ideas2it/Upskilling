package com.upskilling.experiment.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for creating or updating a Project.
 *
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Data
public class ProjectRequestDTO {

    @NotBlank(message = "Project title cannot be empty")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    // Optional list of member IDs to add/remove during an update
    private Set<Long> memberIds;
}
