package com.upskilling.experiment.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * DTO representing an Attachment for API responses.
 *
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Data
public class AttachmentResponseDTO {
    private Long id;
    private Long taskId;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private Long uploadedByUserId;
    private String uploadedByUsername;
    private LocalDateTime uploadDate;

}