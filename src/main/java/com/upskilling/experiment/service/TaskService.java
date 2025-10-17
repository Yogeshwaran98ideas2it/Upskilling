package com.upskilling.experiment.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.upskilling.experiment.dto.request.CommentRequestDTO;
import com.upskilling.experiment.dto.request.TaskCreateRequestDTO;
import com.upskilling.experiment.dto.request.TaskUpdateRequestDTO;
import com.upskilling.experiment.dto.response.CommentResponseDTO;
import com.upskilling.experiment.dto.response.TaskResponseDTO;
import com.upskilling.experiment.entity.Task;
import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.enums.TaskStatus;
import com.upskilling.experiment.enums.TaskType;
import com.upskilling.experiment.enums.TicketType;

public interface TaskService {

    TaskResponseDTO createTask(TaskCreateRequestDTO request, User creator);
    TaskResponseDTO getTaskById(Long id);
    List<TaskResponseDTO> getTasksByListId(Long taskListId);
    TaskResponseDTO updateTask(Long id, TaskUpdateRequestDTO request, User updater);
    void deleteTask(Long id);

    CommentResponseDTO addCommentToTask(Long taskId, CommentRequestDTO request, User commenter);
    List<CommentResponseDTO> getCommentsByTaskId(Long taskId);
}
