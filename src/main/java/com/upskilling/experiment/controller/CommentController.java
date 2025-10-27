package com.upskilling.experiment.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.upskilling.experiment.dto.request.CommentRequestDTO;
import com.upskilling.experiment.dto.response.CommentResponseDTO;
import com.upskilling.experiment.service.CommentService;

/**
 * REST Controller for comment management operations.
 * Provides endpoints for retrieving and adding comments to tasks.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    /** Service for handling comment business logic operations */
    @Autowired
    private CommentService commentService;

    /**
     * Retrieve all comments for a specific task
     * Returns comments ordered by timestamp (most recent first)
     * 
     * @param taskId The unique identifier of the task
     * @return List of comment response DTOs
     */
    @GetMapping
    public List<CommentResponseDTO> getComments(@PathVariable Long taskId) {
        return commentService.getCommentsByTask(taskId);
    }

    /**
     * Add a new comment to a task
     * Creates a comment linked to the task and the current user
     * 
     * @param taskId The unique identifier of the task to comment on
     * @param request DTO containing the comment content
     * @return ResponseEntity with the created comment details (HTTP 200)
     */
    @PostMapping
    public ResponseEntity<CommentResponseDTO> addComment(
            @PathVariable Long taskId,
            @RequestBody CommentRequestDTO request
    ) {
        CommentResponseDTO newComment = commentService.addComment(taskId, request);
        return ResponseEntity.ok(newComment);
    }
}