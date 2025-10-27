
package com.upskilling.experiment.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.upskilling.experiment.dto.request.TagRequestDTO;
import com.upskilling.experiment.dto.response.TagResponseDTO;
import com.upskilling.experiment.entity.Tag;
import com.upskilling.experiment.mapper.TagMapper;
import com.upskilling.experiment.repository.TagRepository;
import com.upskilling.experiment.service.TagService;

/**
 * Service implementation for tag management business logic.
 * Handles CRUD operations for tags and provides utility methods for tag creation/finding.
 * Tags are reusable across tasks and are case-insensitive for uniqueness.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

    /** Repository for database operations on Tag entities */
    private final TagRepository tagRepository;
    
    /** Mapper for converting between Tag entities and DTOs */
    private final TagMapper tagMapper;

    @Override
    public TagResponseDTO createTag(TagRequestDTO request) {
        if (tagRepository.findAll().stream().anyMatch(t -> t.getName().equalsIgnoreCase(request.getName()))) {
            throw new RuntimeException("Tag name already exists.");
        }
        Tag tag = new Tag(request.getName());
        return tagMapper.toDto(tagRepository.save(tag));
    }

    @Override
    public TagResponseDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with ID: " + id));
        return tagMapper.toDto(tag);
    }

    @Override
    public List<TagResponseDTO> getAllTags() {
        return tagMapper.toDtoList(tagRepository.findAll());
    }

    @Override
    public TagResponseDTO updateTag(Long id, TagRequestDTO request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with ID: " + id));
        
        tag.setName(request.getName());
        return tagMapper.toDto(tagRepository.save(tag));
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Set<Tag> findOrCreateTags(Set<String> tagNames) {
        Set<Tag> tags = new HashSet<>();
        if (tagNames == null) return tags;

        for (String name : tagNames) {
            // Find existing tag by name (case-insensitive search for simplicity)
            Tag tag = tagRepository.findAll().stream()
                    .filter(t -> t.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseGet(() -> tagRepository.save(new Tag(name)));
            tags.add(tag);
        }
        return tags;
    }
}