package com.upskilling.experiment.dto.request;

import com.upskilling.experiment.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for creating a new Task.
 */
@Data
public class TaskCreateRequestDTO {
    @NotBlank(message = "Task title cannot be empty")
    @Size(max = 150, message = "Title must be less than 150 characters")
    private String title;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @NotNull(message = "TaskList ID is required for a new task")
    private Long taskListId;

    private TaskPriority priority = TaskPriority.MEDIUM;

    private LocalDate dueDate;

    private Long assigneeId; // Optional: ID of the user to assign the task to

    private Set<String> tags; // Tags names (Strings) to be added to the task
}
