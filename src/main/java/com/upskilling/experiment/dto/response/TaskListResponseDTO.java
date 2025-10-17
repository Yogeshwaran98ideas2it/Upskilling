package com.upskilling.experiment.dto.response;

import lombok.Data;
import java.util.List;

/**
 * DTO for responding with TaskList data.
 */
@Data
public class TaskListResponseDTO {
    private Long id;
    private String title;
    private Long projectId;
    private List<TaskResponseDTO> tasks;
}