package com.upskilling.experiment.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO representing a Project for API responses.
 */
@Data
public class ProjectResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private UserResponseDTO creator;
    private List<UserResponseDTO> members;
}
