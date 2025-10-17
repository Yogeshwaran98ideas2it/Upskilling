package com.upskilling.experiment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.upskilling.experiment.dto.request.ProjectRequestDTO;
import com.upskilling.experiment.dto.response.ProjectResponseDTO;
import com.upskilling.experiment.dto.response.UserResponseDTO;
import com.upskilling.experiment.entity.Project;
import com.upskilling.experiment.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    /**
     * Maps a Project entity to a ProjectDto.
     * * The expression ensures that if project.getDescription() is null, an empty string ("") is used,
     * matching the original manual implementation logic.
     * The members list mapping is handled automatically by MapStruct using the 'toUserDto' method.
     *
     * @param project The Project entity to convert.
     * @return The resulting ProjectDto, or null if the input is null.
     */
    @Mapping(target = "description", expression = "java(project.getDescription() == null ? \"\" : project.getDescription())")
    ProjectResponseDTO toDto(Project project);

    /**
     * Maps a list of Project entities to a list of ProjectDtos.
     * MapStruct handles null checking for the list itself and delegates item mapping to toDto.
     */
    List<ProjectResponseDTO> toDtoList(List<Project> projects);

    /**
     * Maps a User entity to a UserDto.
     * * @param user The User entity to convert.
     * @return The resulting UserDto, or null if the input is null.
     */
//    UserResponseDTO toUserDto(User user);

    /**
     * Maps a ProjectRequest DTO to a Project Entity for creation.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "taskLists", ignore = true)
    @Mapping(target = "activityLogs", ignore = true)
    Project toEntity(ProjectRequestDTO request);

    /**
     * Updates an existing Project entity from a ProjectRequest DTO.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "taskLists", ignore = true)
    @Mapping(target = "activityLogs", ignore = true)
    void updateEntityFromRequest(ProjectRequestDTO request, @MappingTarget Project project);
}
