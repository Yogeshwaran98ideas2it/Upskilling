package com.upskilling.experiment.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO representing a Comment for API responses.
 */
@Data
public class CommentResponseDTO {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private Long taskId;
    private UserResponseDTO user; // User who wrote the comment
}
