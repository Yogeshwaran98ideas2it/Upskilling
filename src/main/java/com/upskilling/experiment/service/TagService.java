package com.upskilling.experiment.service;

import java.util.Set;
import java.util.List;

import com.upskilling.experiment.dto.request.TagRequestDTO;
import com.upskilling.experiment.dto.response.TagResponseDTO;
import com.upskilling.experiment.entity.Tag;

public interface TagService {
    TagResponseDTO createTag(TagRequestDTO request);
    TagResponseDTO getTagById(Long id);
    List<TagResponseDTO> getAllTags();
    TagResponseDTO updateTag(Long id, TagRequestDTO request);
    void deleteTag(Long id);

    // Helper for Task service
    Set<Tag> findOrCreateTags(Set<String> tagNames);
}
