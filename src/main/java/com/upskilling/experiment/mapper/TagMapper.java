package com.upskilling.experiment.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.upskilling.experiment.dto.request.TagRequestDTO;
import com.upskilling.experiment.dto.response.TagResponseDTO;
import com.upskilling.experiment.entity.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    /**
     * Maps a Tag entity to a TagDto.
     */
    TagResponseDTO toDto(Tag tag);

    /**
     * Maps a list of Tag entities to a list of TagDtos.
     */
    List<TagResponseDTO> toDtoList(List<Tag> tags);

    /**
     * Maps a TagRequest DTO to a Tag entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true) // Ignored in request
    Tag toEntity(TagRequestDTO request);
}