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

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final ActivityLogService activityLogService;

    private final UserRepository userRepository;


    @Autowired
    private ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ActivityLogService activityLogService, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.activityLogService = activityLogService;
        this.userRepository = userRepository;
    }

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

    @Override
    public List<ProjectResponseDTO> getAllProjects() {
        return projectMapper.toDtoList(projectRepository.findAll());
    }

    @Override
    public ProjectResponseDTO getProjectById(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        return projectMapper.toDto(project);
    }

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

    @Override
    public void deleteProject(Long projectId,User deleter) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));

        activityLogService.createLog("PROJECT_DELETED",
                "Project '" + project.getTitle() + "' deleted.",
                deleter, null, project);
        projectRepository.deleteById(projectId);
    }
}
