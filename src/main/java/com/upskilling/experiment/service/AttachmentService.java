package com.upskilling.experiment.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.upskilling.experiment.dto.response.AttachmentResponseDTO;
import com.upskilling.experiment.entity.Attachment;

/**
 * Service interface for file attachment management operations.
 * Defines methods for uploading, storing, and retrieving file attachments.
 * Files are stored with UUID-based unique naming to prevent conflicts.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public interface AttachmentService {
    AttachmentResponseDTO storeFile(Long taskId, Long userId, MultipartFile file) throws IOException;
    List<AttachmentResponseDTO> getAttachmentsByTask(Long taskId);
    AttachmentResponseDTO findById(Long attachmentId);
    Path loadFileAsResource(String storagePath);
    Attachment getAttachmentEntityForDownload(Long attachmentId);
}