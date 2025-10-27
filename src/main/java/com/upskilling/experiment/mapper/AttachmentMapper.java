package com.upskilling.experiment.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.upskilling.experiment.dto.response.AttachmentResponseDTO;
import com.upskilling.experiment.entity.Attachment;


@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "uploadedBy.id", target = "uploadedByUserId")
    @Mapping(source = "uploadedBy.username", target = "uploadedByUsername")
    AttachmentResponseDTO toDto(Attachment attachment);
    
    List<AttachmentResponseDTO> toDtoList(List<Attachment> attachments);
}