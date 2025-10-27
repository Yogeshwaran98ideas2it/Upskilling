package com.upskilling.experiment.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import com.upskilling.experiment.config.FileStorageProperties;
import com.upskilling.experiment.dto.response.AttachmentResponseDTO;
import com.upskilling.experiment.entity.Attachment;
import com.upskilling.experiment.entity.Task;
import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.mapper.AttachmentMapper;
import com.upskilling.experiment.repository.AttachmentRepository;
import com.upskilling.experiment.repository.TaskRepository;
import com.upskilling.experiment.repository.UserRepository;
import com.upskilling.experiment.service.ActivityLogService;
import com.upskilling.experiment.service.AttachmentService;

/**
 * Service implementation for file attachment management.
 * Handles file upload, storage, and retrieval operations.
 * Files are stored in a configurable directory with UUID-based unique naming to prevent conflicts.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {
    
    /** Absolute path to the directory where uploaded files are stored */
    private final Path fileStorageLocation;

    /** Repository for database operations on Attachment entities */
    @Autowired private AttachmentRepository attachmentRepository;
    
    /** Repository for database operations on Task entities */
    @Autowired private TaskRepository taskRepository;
    
    /** Repository for database operations on User entities */
    @Autowired private UserRepository userRepository;
    
    /** Service for activity log operations */
    @Autowired private ActivityLogService activityLogService;
    
    /** Mapper for converting between Attachment entities and DTOs */
    @Autowired private AttachmentMapper attachmentMapper;

    /**
     * Constructor that initializes the file storage location
     * Creates the upload directory if it doesn't exist
     * The directory is configured via FileStorageProperties (application.yml)
     * 
     * @param fileStorageProperties Configuration properties containing upload directory path
     * @throws IOException if the storage directory cannot be created
     */
    public AttachmentServiceImpl(FileStorageProperties fileStorageProperties) throws IOException {
        // Convert the configured upload directory to an absolute, normalized path
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            // Create the directory structure if it doesn't exist
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new IOException("Could not create the storage directory.", ex);
        }
    }

    /**
     * Store an uploaded file and create an attachment record in the database
     * Files are saved with UUID-prefixed names to ensure uniqueness and prevent conflicts
     * 
     * @param taskId The unique identifier of the task to attach the file to
     * @param userId The unique identifier of the user uploading the file
     * @param file The multipart file to store
     * @return AttachmentResponseDTO with file metadata
     * @throws IOException if file I/O operations fail
     * @throws RuntimeException if task or user not found
     */
    @Override
    public AttachmentResponseDTO storeFile(Long taskId, Long userId, MultipartFile file) throws IOException {
        // Get the original filename and sanitize it (remove special characters)
        String originalFileName = file.getOriginalFilename();
        String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        
        // Generate a unique filename using UUID to prevent name conflicts
        // Format: {UUID}_{sanitized-filename}
        // Example: 0ac96f17-4bef-4fe9-990f-f8cad9eafb1a_document.pdf
        String uniqueFileName = UUID.randomUUID().toString() + "_" + sanitizedFileName;

        // Validate that the task and user exist
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Build the full path where the file will be stored
        Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);

        // Copy the uploaded file to the target location
        // StandardCopyOption.REPLACE_EXISTING ensures that if a file with the same UUID exists, it will be replaced
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Create an attachment record in the database
        Attachment attachment = new Attachment();
        attachment.setTask(task);
        attachment.setFileName(originalFileName); // Store original filename for display
        attachment.setStoragePath(targetLocation.toString()); // Store full path for retrieval
        attachment.setFileType(file.getContentType()); // MIME type (e.g., "application/pdf")
        attachment.setFileSize(file.getSize()); // File size in bytes
        attachment.setUploadedBy(user);

        // Save the attachment record to the database
        Attachment savedAttachment = attachmentRepository.save(attachment);

        // Log the file upload activity for audit trail
        activityLogService.createLog(
                "ATTACHMENT_ADDED",
                "File uploaded: " + originalFileName,
                user,
                task,
                null
        );

        return attachmentMapper.toDto(savedAttachment);
    }

    @Override
    public List<AttachmentResponseDTO> getAttachmentsByTask(Long taskId) {
        List<Attachment> attachments = attachmentRepository.findByTaskId(taskId);
        return attachmentMapper.toDtoList(attachments);
    }

    @Override
    public AttachmentResponseDTO findById(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));
        return attachmentMapper.toDto(attachment);
    }

    @Override
    public Path loadFileAsResource(String storagePath) {
        return Paths.get(storagePath).normalize();
    }

    @Override
    public Attachment getAttachmentEntityForDownload(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));
    }
}