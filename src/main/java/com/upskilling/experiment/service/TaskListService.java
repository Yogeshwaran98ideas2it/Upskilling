package com.upskilling.experiment.service;

import java.util.List;

import com.upskilling.experiment.dto.request.TaskListRequestDTO;
import com.upskilling.experiment.dto.response.TaskListResponseDTO;

public interface TaskListService {
    TaskListResponseDTO createTaskList(TaskListRequestDTO request);
    TaskListResponseDTO getTaskListById(Long id);
    List<TaskListResponseDTO> getListsByProjectId(Long projectId);
    TaskListResponseDTO updateTaskList(Long id, TaskListRequestDTO request);
    void deleteTaskList(Long id);
}