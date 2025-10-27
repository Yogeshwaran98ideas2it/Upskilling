/**
 * Repository interface for database operations on Comment entities.
 * Extends JpaRepository to provide CRUD operations.
 * Includes custom query method for retrieving comments by task ID.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
package com.upskilling.experiment.repository;

import com.upskilling.experiment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Find all comments for a specific task, ordered by timestamp ascending
     * 
     * @param taskId The unique identifier of the task
     * @return List of comments for the task, ordered by timestamp (oldest first)
     */
    List<Comment> findByTaskIdOrderByTimestampAsc(Long taskId);
}