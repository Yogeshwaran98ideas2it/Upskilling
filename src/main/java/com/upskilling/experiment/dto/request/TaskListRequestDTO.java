package com.upskilling.experiment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for creating or updating a TaskList.
 *
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Data
public class TaskListRequestDTO {
    @NotBlank(message = "List title cannot be empty")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    // The Project ID this list belongs to (required for creation)
    @NotNull(message = "Project ID is required")
    private Long projectId;
}
