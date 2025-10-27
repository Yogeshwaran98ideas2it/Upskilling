package com.upskilling.experiment.service;

import java.util.List;

import com.upskilling.experiment.dto.request.CommentRequestDTO;
import com.upskilling.experiment.dto.response.CommentResponseDTO;

/**
 * Service interface for comment management operations.
 * Defines methods for adding and retrieving comments on tasks.
 * Comments enable collaboration and communication within the system.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public interface CommentService {
    CommentResponseDTO addComment(Long taskId, CommentRequestDTO requestDTO);
    List<CommentResponseDTO> getCommentsByTask(Long taskId);
}