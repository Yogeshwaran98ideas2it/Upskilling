package com.upskilling.experiment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for creating a Comment.
 */
@Data
public class CommentRequestDTO {
    @NotBlank(message = "Comment content cannot be empty")
    private String content;

    private Long userId;

}
