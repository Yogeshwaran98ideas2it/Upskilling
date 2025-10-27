package com.upskilling.experiment.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.upskilling.experiment.dto.request.CommentRequestDTO;
import com.upskilling.experiment.dto.response.CommentResponseDTO;
import com.upskilling.experiment.entity.Comment;
import com.upskilling.experiment.entity.Task;
import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.mapper.CommentMapper;
import com.upskilling.experiment.repository.CommentRepository;
import com.upskilling.experiment.repository.TaskRepository;
import com.upskilling.experiment.repository.UserRepository;
import com.upskilling.experiment.service.ActivityLogService;
import com.upskilling.experiment.service.CommentService;

/**
 * Service implementation for comment management business logic.
 * Handles CRUD operations for task comments and activity logging.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Service
public class CommentServiceImpl implements CommentService {

    /** Repository for database operations on Comment entities */
    @Autowired private CommentRepository commentRepository;
    
    /** Repository for database operations on Task entities */
    @Autowired private TaskRepository taskRepository;
    
    /** Repository for database operations on User entities */
    @Autowired private UserRepository userRepository;
    
    /** Service for activity log operations */
    @Autowired private ActivityLogService activityLogService;
    
    /** Mapper for converting between Comment entities and DTOs */
    @Autowired private CommentMapper commentMapper;

    /**
     * Add a new comment to a task
     * Creates a comment entity, associates it with the task and user, and logs the activity
     * 
     * @param taskId The unique identifier of the task to comment on
     * @param request DTO containing comment content and user ID
     * @return CommentResponseDTO with created comment details
     * @throws RuntimeException if task or user not found
     */
    @Override
    public CommentResponseDTO addComment(Long taskId, CommentRequestDTO request) {
        // Validate and fetch the task entity
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        // Validate and fetch the user entity
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        // Convert DTO to entity using MapStruct mapper
        Comment comment = commentMapper.toEntity(request);
        
        // Set relationship entities (task and user)
        comment.setTask(task);
        comment.setUser(user);

        // Persist the comment to the database
        Comment savedComment = commentRepository.save(comment);

        // Log the comment creation activity for audit trail
        activityLogService.createLog(
            "COMMENT_ADDED", 
            "New comment added by " + user.getUsername(), 
            user, 
            task, 
            null
        );

        // Convert entity back to response DTO and return
        return commentMapper.toDto(savedComment);
    }

    /**
     * Retrieve all comments for a specific task
     * Returns comments ordered by timestamp in ascending order (oldest first)
     * 
     * @param taskId The unique identifier of the task
     * @return List of comment response DTOs
     */
    @Override
    public List<CommentResponseDTO> getCommentsByTask(Long taskId) {
        // Fetch all comments for the task, ordered by timestamp
        List<Comment> comments = commentRepository.findByTaskIdOrderByTimestampAsc(taskId);
        // Convert entities to DTOs and return
        return commentMapper.toDtoList(comments);
    }
}