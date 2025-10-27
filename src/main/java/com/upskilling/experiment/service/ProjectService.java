package com.upskilling.experiment.service;

import java.util.List;

import com.upskilling.experiment.dto.request.ProjectRequestDTO;
import com.upskilling.experiment.dto.response.ProjectResponseDTO;
import com.upskilling.experiment.entity.User;

/**
 * Service interface for project management operations.
 * Defines methods for CRUD operations on projects and team member management.
 * Projects serve as containers for organizing tasks and task lists.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public interface ProjectService {

    ProjectResponseDTO createProject(ProjectRequestDTO request, User currentUser);

    List<ProjectResponseDTO> getAllProjects();

    ProjectResponseDTO getProjectById(Long projectId);

    ProjectResponseDTO updateProject(Long projectId, ProjectRequestDTO request,  User updater);

    void deleteProject(Long projectId, User deleter);
}
