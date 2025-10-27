package com.upskilling.experiment.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upskilling.experiment.dto.request.ProjectRequestDTO;
import com.upskilling.experiment.dto.response.ProjectResponseDTO;
import com.upskilling.experiment.entity.Project;
import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.mapper.ProjectMapper;
import com.upskilling.experiment.repository.ProjectRepository;
import com.upskilling.experiment.repository.UserRepository;
import com.upskilling.experiment.service.ActivityLogService;
import com.upskilling.experiment.service.ProjectService;

/**
 * Service implementation for project management business logic.
 * Handles CRUD operations for projects, team member management, and activity logging.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    /** Repository for database operations on Project entities */
    private final ProjectRepository projectRepository;

    /** Service for activity log operations */
    private final ActivityLogService activityLogService;

    /** Repository for database operations on User entities */
    private final UserRepository userRepository;

    /** Mapper for converting between Project entities and DTOs */
    @Autowired
    private ProjectMapper projectMapper;

    /**
     * Constructor for dependency injection
     * 
     * @param projectRepository The project repository to be injected
     * @param activityLogService The activity log service to be injected
     * @param userRepository The user repository to be injected
     */
    public ProjectServiceImpl(ProjectRepository projectRepository, ActivityLogService activityLogService, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.activityLogService = activityLogService;
        this.userRepository = userRepository;
    }

    /**
     * Create a new project with optional team members
     * Automatically sets the creator as a member and logs the activity
     * 
     * @param request DTO containing project details and member IDs
     * @param creator The user creating the project
     * @return ProjectResponseDTO with created project details
     * @throws EntityNotFoundException if any member ID doesn't exist
     */
    @Override
    public ProjectResponseDTO createProject(ProjectRequestDTO request, User creator) {
        // 1. Map DTO to Entity
        Project project = projectMapper.toEntity(request);

        // 2. Set creator and add creator as the first member
        project.setCreator(creator);
        project.getMembers().add(creator);

        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            Set<User> members = request.getMemberIds().stream()
                    .map(memberId -> userRepository.findById(memberId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + memberId)))
                    .collect(Collectors.toSet());
            project.getMembers().addAll(members);
        }

        // 3. Save and map to DTO
        Project savedProject = projectRepository.save(project);

        // 4. Log activity
        activityLogService.createLog("PROJECT_CREATED",
                "Project '" + savedProject.getTitle() + "' created.",
                creator, null, savedProject);

        return projectMapper.toDto(savedProject);
    }

    /**
     * Retrieve all projects in the system
     * Returns list of all projects with their creators and members
     * 
     * @return List of all project response DTOs
     */
    @Override
    public List<ProjectResponseDTO> getAllProjects() {
        return projectMapper.toDtoList(projectRepository.findAll());
    }

    /**
     * Retrieve a specific project by its unique identifier
     * Returns project details including creator and members
     * 
     * @param projectId The unique identifier of the project
     * @return ProjectResponseDTO with project details
     * @throws EntityNotFoundException if project with given ID doesn't exist
     */
    @Override
    public ProjectResponseDTO getProjectById(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        return projectMapper.toDto(project);
    }

    /**
     * Update an existing project
     * Allows modification of title, description, and member list
     * Logs activity when members are updated
     * 
     * @param projectId The unique identifier of the project to update
     * @param request DTO containing updated project details
     * @param updater The user performing the update
     * @return ProjectResponseDTO with updated project details
     * @throws EntityNotFoundException if project or any new member ID doesn't exist
     */
    @Override
    public ProjectResponseDTO updateProject(Long projectId, ProjectRequestDTO request, User updater) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));

        // Simple field updates (title, description)
        projectMapper.updateEntityFromRequest(request, project);

        // Handle members update (if memberIds are provided)
        if (request.getMemberIds() != null) {
            Set<User> newMembers = request.getMemberIds().stream()
                    .map(memberId -> userRepository.findById(memberId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + memberId)))
                    .collect(Collectors.toSet());

            project.setMembers(newMembers);

            activityLogService.createLog("PROJECT_MEMBERS_UPDATED",
                    "Project members list updated.",
                    project.getCreator(), null, project);
        }

        return projectMapper.toDto(projectRepository.save(project));
    }

    /**
     * Delete a project by its unique identifier
     * Permanently removes the project and all associated task lists and tasks
     * Logs the deletion activity before removing from database
     * 
     * @param projectId The unique identifier of the project to delete
     * @param deleter The user performing the deletion
     * @throws EntityNotFoundException if project with given ID doesn't exist
     */
    @Override
    public void deleteProject(Long projectId,User deleter) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));

        // Log the deletion activity for audit trail
        activityLogService.createLog("PROJECT_DELETED",
                "Project '" + project.getTitle() + "' deleted.",
                deleter, null, project);
        
        // Delete the project from the database
        projectRepository.deleteById(projectId);
    }
}
