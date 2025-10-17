package com.upskilling.experiment.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO representing an Activity Log entry for API responses.
 */
@Data
public class ActivityLogResponseDTO {
    private Long id;
    private String activityType;
    private String details;
    private LocalDateTime timestamp;
    private UserResponseDTO user;
    private Long taskId;
    private Long projectId; // User who performed the action
}
