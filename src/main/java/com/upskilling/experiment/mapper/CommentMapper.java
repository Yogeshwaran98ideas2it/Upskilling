package com.upskilling.experiment.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.upskilling.experiment.dto.request.CommentRequestDTO;
import com.upskilling.experiment.dto.response.CommentResponseDTO;
import com.upskilling.experiment.entity.Comment;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    /**
     * Maps a Comment entity to a CommentDto.
     * Maps the user entity to UserDto using the injected UserMapper.
     */
    @Mapping(source = "user", target = "user")
    @Mapping(source = "task.id", target = "taskId")
    CommentResponseDTO toDto(Comment comment);

    /**
     * Maps a list of Comment entities to a list of CommentDtos.
     */
    List<CommentResponseDTO> toDtoList(List<Comment> comments);

    /**
     * Maps a CommentRequest DTO to a Comment entity.
     * Note: 'user' and 'task' fields must be set in the service layer
     * as entities cannot be mapped from simple IDs in MapStruct without custom logic.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    Comment toEntity(CommentRequestDTO request);
}