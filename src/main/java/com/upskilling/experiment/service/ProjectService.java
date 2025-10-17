package com.upskilling.experiment.service;

import java.util.List;

import com.upskilling.experiment.dto.request.ProjectRequestDTO;
import com.upskilling.experiment.dto.response.ProjectResponseDTO;
import com.upskilling.experiment.entity.User;

public interface ProjectService {

    ProjectResponseDTO createProject(ProjectRequestDTO request, User currentUser);

    List<ProjectResponseDTO> getAllProjects();

    ProjectResponseDTO getProjectById(Long projectId);

    ProjectResponseDTO updateProject(Long projectId, ProjectRequestDTO request,  User updater);

    void deleteProject(Long projectId, User deleter);
}
