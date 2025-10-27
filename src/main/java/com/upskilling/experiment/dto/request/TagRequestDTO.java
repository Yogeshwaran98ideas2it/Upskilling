package com.upskilling.experiment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for creating or updating a Tag.
 *
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Data
public class TagRequestDTO {
    @NotBlank(message = "Tag name cannot be empty")
    @Size(max = 50, message = "Tag name must be less than 50 characters")
    private String name;
}
