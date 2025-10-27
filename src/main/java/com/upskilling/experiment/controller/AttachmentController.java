package com.upskilling.experiment.controller;

import com.upskilling.experiment.dto.response.AttachmentResponseDTO;
import com.upskilling.experiment.entity.Attachment;
import com.upskilling.experiment.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * REST Controller for file attachment management operations.
 * Handles file upload, retrieval, and download for task attachments.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/tasks/{taskId}/attachments")
public class AttachmentController {

    /** Service for file attachment business logic operations */
    @Autowired
    private AttachmentService attachmentService;

    /**
     * Upload a file attachment to a specific task
     * Supports multipart/form-data file uploads
     * Files are stored with UUID-prefixed names to ensure uniqueness
     * 
     * @param taskId The unique identifier of the task to attach the file to
     * @param file The multipart file to upload (passed as form data)
     * @param userId The ID of the user uploading the file (passed as header X-User-Id)
     * @return ResponseEntity with attachment metadata (file name, size, type, upload date)
     * @throws IOException if file I/O operations fail
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponseDTO> uploadFile(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file,
            @RequestHeader("X-User-Id") Long userId
    ) throws IOException {
        AttachmentResponseDTO attachment = attachmentService.storeFile(taskId, userId, file);
        return ResponseEntity.ok(attachment);
    }
    
    /**
     * Retrieve all file attachments for a specific task
     * Returns metadata about each attachment (name, size, type, upload date, uploaded by)
     * 
     * @param taskId The unique identifier of the task
     * @return List of attachment metadata DTOs
     */
    @GetMapping
    public List<AttachmentResponseDTO> getAttachments(@PathVariable Long taskId) {
        return attachmentService.getAttachmentsByTask(taskId);
    }

    /**
     * Download a specific file attachment
     * Returns the actual file content as a downloadable resource
     * Sets appropriate content-type and content-disposition headers
     * 
     * @param attachmentId The unique identifier of the attachment to download
     * @return ResponseEntity containing the file as a Resource with download headers
     * @throws IOException if file I/O operations fail
     * @throws RuntimeException if file is not found or not readable
     */
    @GetMapping("/{attachmentId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long attachmentId) throws IOException {
        // Fetch attachment entity to get storage path and metadata
        Attachment attachment = attachmentService.getAttachmentEntityForDownload(attachmentId);
        
        // Load the file from disk as a Resource
        Path filePath = attachmentService.loadFileAsResource(attachment.getStoragePath()); 
        Resource resource = new UrlResource(filePath.toUri());
        
        // Validate that file exists and is readable
        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("File not found or not readable: " + attachment.getFileName());
        }
        
        // Return file with appropriate headers for download
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(resource);
    }
}