package com.upskilling.experiment.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.upskilling.experiment.dto.response.ActivityLogResponseDTO;
import com.upskilling.experiment.entity.ActivityLog;

/**
 * Mapper for converting ActivityLog Entities to ActivityLog DTOs.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ActivityLogMapper {

    /**
     * Converts an ActivityLog entity to an ActivityLogDto (Response DTO).
     */
    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "projectId", source = "project.id")
    ActivityLogResponseDTO toDto(ActivityLog activityLog);

    /**
     * Converts a list of ActivityLog entities to a list of ActivityLog DTOs.
     */
    List<ActivityLogResponseDTO> toDtoList(List<ActivityLog> activityLogs);
}
