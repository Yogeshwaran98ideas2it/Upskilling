package com.upskilling.experiment.dto.response;

import com.upskilling.experiment.enums.TaskPriority;
import com.upskilling.experiment.enums.TaskStatus;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * DTO representing a Task for API responses.
 */
@Data
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private LocalDateTime createdAt;

    // Relationships represented by nested DTOs or IDs
    private Long taskListId;
    private UserResponseDTO assignee;
    private UserResponseDTO creator;
    private Set<TagResponseDTO> tags;
    private List<CommentResponseDTO> comments;
}
