package com.upskilling.experiment.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.upskilling.experiment.dto.request.RegisterRequestDTO;
import com.upskilling.experiment.dto.response.UserResponseDTO;
import com.upskilling.experiment.entity.User;

/**
 * Mapper for converting between User DTOs and User Entities.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts a User entity to a UserDto (Response DTO).
     */
    UserResponseDTO toDto(User user);

    /**
     * Converts a list of User entities to a list of User DTOs.
     */
    List<UserResponseDTO> toDtoList(List<User> users);

//    /**
//     * Converts a UserRegistrationRequest DTO to a User entity.
//     */
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "role", constant = "USER") // Set default role
//    @Mapping(target = "password", source = "password") // Map password field
//    @Mapping(target = "projects", ignore = true)
//    @Mapping(target = "createdTasks", ignore = true)
//    @Mapping(target = "assignedTasks", ignore = true)
//    @Mapping(target = "comments", ignore = true)
//    @Mapping(target = "activityLogs", ignore = true)
//    User toEntity(RegisterRequestDTO request);
}
