package com.upskilling.experiment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Attachment entity representing file attachments on tasks.
 * Stores metadata about uploaded files including original filename, storage path, file type, and size.
 * Files are stored with UUID-based unique naming to prevent conflicts.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@NoArgsConstructor
public class Attachment {
    /** Unique identifier for the attachment */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Task this attachment is linked to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    /** Original filename of the uploaded file */
    private String fileName;

    /** Full path where the file is stored on disk (with UUID prefix) */
    private String storagePath;

    /** MIME type of the file (e.g., image/jpeg, application/pdf, text/plain) */
    private String fileType;

    /** File size in bytes */
    private Long fileSize;

    /** User who uploaded the file */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_user_id")
    private User uploadedBy;
    
    /** Timestamp when file was uploaded */
    private LocalDateTime uploadDate = LocalDateTime.now();

}