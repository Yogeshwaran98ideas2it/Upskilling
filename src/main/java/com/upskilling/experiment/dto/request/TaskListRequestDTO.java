package com.upskilling.experiment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskListRequestDTO {
    @NotBlank(message = "List title cannot be empty")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    // The Project ID this list belongs to (required for creation)
    @NotNull(message = "Project ID is required")
    private Long projectId;
}
