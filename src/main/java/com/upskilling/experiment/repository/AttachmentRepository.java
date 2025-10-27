/**
 * Repository interface for database operations on Attachment entities.
 * Extends JpaRepository to provide CRUD operations.
 * Includes custom query method for retrieving attachments by task ID.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
package com.upskilling.experiment.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.upskilling.experiment.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    /**
     * Find all attachments for a specific task
     * 
     * @param taskId The unique identifier of the task
     * @return List of attachments for the task
     */
    List<Attachment> findByTaskId(Long taskId);
}