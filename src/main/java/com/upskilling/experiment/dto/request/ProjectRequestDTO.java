package com.upskilling.experiment.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectRequestDTO {

    @NotBlank(message = "Project title cannot be empty")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    // Optional list of member IDs to add/remove during an update
    private Set<Long> memberIds;
}
