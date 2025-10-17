package com.upskilling.experiment.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.upskilling.experiment.dto.request.TaskListRequestDTO;
import com.upskilling.experiment.dto.response.TaskListResponseDTO;
import com.upskilling.experiment.entity.TaskList;

@Mapper(componentModel = "spring", uses = {TaskMapper.class})
public interface TaskListMapper {

    TaskListMapper INSTANCE = Mappers.getMapper(TaskListMapper.class);

    /**
     * Maps a TaskList entity to a TaskListDto.
     * Maps the tasks list using the injected TaskMapper.
     */
    @Mapping(source = "project.id", target = "projectId")
    TaskListResponseDTO toDto(TaskList taskList);

    /**
     * Maps a list of TaskList entities to a list of TaskListDtos.
     */
    List<TaskListResponseDTO> toDtoList(List<TaskList> taskLists);

    /**
     * Maps a TaskListRequest DTO to a TaskList entity.
     * Note: 'project' field must be set in the service layer using projectId.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    TaskList toEntity(TaskListRequestDTO request);
}