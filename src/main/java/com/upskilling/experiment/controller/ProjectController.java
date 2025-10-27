package com.upskilling.experiment.controller;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upskilling.experiment.dto.request.ProjectRequestDTO;
import com.upskilling.experiment.dto.response.ProjectResponseDTO;
import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.mapper.ProjectMapper;
import com.upskilling.experiment.repository.UserRepository;
import com.upskilling.experiment.service.ProjectService;

/**
 * REST Controller for project management operations.
 * Handles CRUD operations for projects and project membership management.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    /** Mapper for converting between DTOs and entities */
    @Autowired
    private ProjectMapper projectMapper;

    /** Service for project business logic operations */
    private final ProjectService projectService;

    /** Repository for user data access - Used to simulate current authenticated user */
    private final UserRepository userRepository;

    /**
     * Constructor for dependency injection
     * 
     * @param projectService The project service to be injected
     * @param userRepository The user repository to be injected
     */
    public ProjectController(ProjectService projectService, UserRepository userRepository) {
        this.projectService = projectService;
        this.userRepository = userRepository;
    }

    /**
     * Utility method to fetch the currently authenticated user
     * In a production environment, this would use @AuthenticationPrincipal annotation
     * Currently simulates authentication by fetching the admin user
     * 
     * @return The currently authenticated User entity
     * @throws EntityNotFoundException if the admin user is not found in the database
     */
    private User getCurrentAuthenticatedUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // Fetch the full User entity from the database to check the role
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found in database."));
    }

    /**
     * Create a new project with optional team members
     * The authenticated user is automatically set as the project creator
     * 
     * @param projectRequestDTO DTO containing project details (title, description, memberIds)
     * @return ResponseEntity with created project details including creator and members (HTTP 201)
     * @throws EntityNotFoundException if any member ID in memberIds doesn't exist
     */
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTO projectRequestDTO) {
        User creator = getCurrentAuthenticatedUser();
        ProjectResponseDTO createdProject = projectService.createProject(projectRequestDTO, creator);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    /**
     * Retrieve all projects in the system
     * Returns list of projects with their creators and members
     * 
     * @return ResponseEntity with list of all projects (HTTP 200)
     */
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    /**
     * Retrieve a specific project by its unique identifier
     * Returns complete project information including creator and members
     * 
     * @param projectId The unique identifier of the project
     * @return ResponseEntity with project details (HTTP 200)
     * @throws EntityNotFoundException if project with given ID doesn't exist
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    /**
     * Update an existing project
     * Allows modification of title, description, and member list
     * 
     * @param projectId The unique identifier of the project to update
     * @param updateRequest DTO containing updated project details
     * @return ResponseEntity with updated project details (HTTP 200)
     * @throws EntityNotFoundException if project or any new member ID doesn't exist
     */
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long projectId, @Valid @RequestBody ProjectRequestDTO updateRequest) {
        User updater = getCurrentAuthenticatedUser();
        ProjectResponseDTO updatedProject = projectService.updateProject(projectId, updateRequest, updater);
        return ResponseEntity.ok(updatedProject);
    }

    /**
     * Delete a project by its unique identifier
     * Permanently removes the project and all associated task lists and tasks
     * Only ADMIN and MANAGER roles can delete projects
     * 
     * @param projectId The unique identifier of the project to delete
     * @return HTTP 204 No Content on successful deletion
     * @throws EntityNotFoundException if project with given ID doesn't exist
     * @throws org.springframework.security.access.AccessDeniedException if user lacks deletion permission
     */
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        User deleter = getCurrentAuthenticatedUser();
        projectService.deleteProject(projectId, deleter);
        return ResponseEntity.noContent().build();
    }

}


