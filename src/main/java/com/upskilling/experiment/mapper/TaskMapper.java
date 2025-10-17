package com.upskilling.experiment.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.upskilling.experiment.dto.request.TaskCreateRequestDTO;
import com.upskilling.experiment.dto.request.TaskUpdateRequestDTO;
import com.upskilling.experiment.dto.response.TaskResponseDTO;
import com.upskilling.experiment.entity.Task;

/**
 * Mapper for converting between Task DTOs and Task Entities.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, TagMapper.class})
public interface TaskMapper {

    /**
     * Converts a Task entity to a TaskDto (Response DTO).
     */
    @Mapping(target = "taskListId", source = "taskList.id")
    TaskResponseDTO toDto(Task task);

    /**
     * Converts a list of Task entities to a list of Task DTOs.
     */
    List<TaskResponseDTO> toDtoList(List<Task> tasks);

    /**
     * Converts a TaskCreateRequest DTO to a Task entity for creation.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "TO_DO") // Default status
    @Mapping(target = "taskList", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "activityLogs", ignore = true)
    Task toEntity(TaskCreateRequestDTO request);

    // Update mapping for TaskUpdateRequest
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "taskList", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "activityLogs", ignore = true)
    void updateEntityFromRequest(TaskUpdateRequestDTO request, @MappingTarget Task task);
}
