package com.upskilling.experiment.service;

import java.util.Set;
import java.util.List;

import com.upskilling.experiment.dto.request.TagRequestDTO;
import com.upskilling.experiment.dto.response.TagResponseDTO;
import com.upskilling.experiment.entity.Tag;

/**
 * Service interface for tag management operations.
 * Defines methods for CRUD operations on tags.
 * Tags are reusable labels for categorizing tasks.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public interface TagService {
    /**
     * Create a new tag
     * 
     * @param request DTO containing tag details
     * @return TagResponseDTO with created tag details
     */
    TagResponseDTO createTag(TagRequestDTO request);
    
    /**
     * Get a tag by its unique identifier
     * 
     * @param id The unique identifier of the tag
     * @return TagResponseDTO with tag details
     */
    TagResponseDTO getTagById(Long id);
    
    /**
     * Get all tags in the system
     * 
     * @return List of all tag response DTOs
     */
    List<TagResponseDTO> getAllTags();
    
    /**
     * Update an existing tag
     * 
     * @param id The unique identifier of the tag to update
     * @param request DTO containing updated tag details
     * @return TagResponseDTO with updated tag details
     */
    TagResponseDTO updateTag(Long id, TagRequestDTO request);
    
    /**
     * Delete a tag by its unique identifier
     * 
     * @param id The unique identifier of the tag to delete
     */
    void deleteTag(Long id);

    /**
     * Helper method for finding existing tags or creating new ones
     * Used by Task service to handle tag assignment on tasks
     * 
     * @param tagNames Set of tag names to find or create
     * @return Set of Tag entities (existing or newly created)
     */
    Set<Tag> findOrCreateTags(Set<String> tagNames);
}
