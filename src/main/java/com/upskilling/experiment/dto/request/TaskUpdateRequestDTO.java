package com.upskilling.experiment.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

import com.upskilling.experiment.enums.TaskPriority;
import com.upskilling.experiment.enums.TaskStatus;
import com.upskilling.experiment.enums.TaskType;
import com.upskilling.experiment.enums.TicketType;

@Data
public class TaskUpdateRequestDTO {
    @NotBlank(message = "Task title cannot be empty")
    @Size(max = 150, message = "Title must be less than 150 characters")
    private String title;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    private TaskStatus status;

    @NotNull(message = "TaskList ID is required for a new task")
    private Long taskListId;

    private TaskPriority priority = TaskPriority.MEDIUM;
    private LocalDate dueDate;
    private Long assigneeId;
    private Set<String> tags;
}