package com.upskilling.experiment.controller;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.upskilling.experiment.dto.request.CommentRequestDTO;
import com.upskilling.experiment.dto.request.TaskCreateRequestDTO;
import com.upskilling.experiment.dto.request.TaskUpdateRequestDTO;
import com.upskilling.experiment.dto.response.CommentResponseDTO;
import com.upskilling.experiment.dto.response.TaskResponseDTO;
import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.repository.UserRepository;
import com.upskilling.experiment.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository; // Used to simulate current user

    // --- Utility method to simulate fetching the authenticated user ---
    private User getCurrentAuthenticatedUser() {
        // In a real application, this would use @AuthenticationPrincipal.
        // We fetch a known user (e.g., 'admin') for testing.
        return userRepository.findByUsername("admin")
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user (admin) not found."));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskCreateRequestDTO request) {
        User creator = getCurrentAuthenticatedUser();
        TaskResponseDTO createdTask = taskService.createTask(request, creator);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        TaskResponseDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/by-list/{taskListId}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByTaskListId(@PathVariable Long taskListId) {
        List<TaskResponseDTO> tasks = taskService.getTasksByListId(taskListId);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskUpdateRequestDTO request) {
        User updater = getCurrentAuthenticatedUser();
        TaskResponseDTO updatedTask = taskService.updateTask(id, request, updater);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    // --- Comment Endpoints ---

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<CommentResponseDTO> addCommentToTask(@PathVariable Long taskId, @Valid @RequestBody CommentRequestDTO request) {
        User commenter = getCurrentAuthenticatedUser();
        CommentResponseDTO comment = taskService.addCommentToTask(taskId, request, commenter);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByTaskId(@PathVariable Long taskId) {
        List<CommentResponseDTO> comments = taskService.getCommentsByTaskId(taskId);
        return ResponseEntity.ok(comments);
    }
}
