package com.upskilling.experiment.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.upskilling.experiment.dto.request.TaskListRequestDTO;
import com.upskilling.experiment.dto.response.TaskListResponseDTO;
import com.upskilling.experiment.entity.Project;
import com.upskilling.experiment.entity.TaskList;
import com.upskilling.experiment.mapper.TaskListMapper;
import com.upskilling.experiment.repository.ProjectRepository;
import com.upskilling.experiment.repository.TaskListRepository;
import com.upskilling.experiment.service.TaskListService;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;
    private final ProjectRepository projectRepository;
    private final TaskListMapper taskListMapper;

    @Override
    public TaskListResponseDTO createTaskList(TaskListRequestDTO request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + request.getProjectId()));

        TaskList list = taskListMapper.toEntity(request);
        list.setProject(project);

        TaskList savedList = taskListRepository.save(list);
        return taskListMapper.toDto(savedList);
    }

    @Override
    public TaskListResponseDTO getTaskListById(Long id) {
        TaskList list = taskListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task list not found with ID: " + id));
        return taskListMapper.toDto(list);
    }

    @Override
    public List<TaskListResponseDTO> getListsByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));

        return project.getTaskLists().stream()
                .map(taskListMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskListResponseDTO updateTaskList(Long id, TaskListRequestDTO request) {
        TaskList list = taskListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task list not found with ID: " + id));
        
        // Update only the title
        list.setTitle(request.getTitle());

        return taskListMapper.toDto(taskListRepository.save(list));
    }

    @Override
    public void deleteTaskList(Long id) {
        TaskList list = taskListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task list not found with ID: " + id));
        
        // Assuming CascadeType.ALL handles deletion of associated tasks in the entity definition
        taskListRepository.delete(list);
    }
}