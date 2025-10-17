package com.upskilling.experiment.controller;

import java.util.List;

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

import com.upskilling.experiment.dto.request.TaskListRequestDTO;
import com.upskilling.experiment.dto.response.TaskListResponseDTO;
import com.upskilling.experiment.service.TaskListService;

@RestController
@RequestMapping("/api/tasklists")
@RequiredArgsConstructor
public class TaskListController {

    private final TaskListService taskListService;

    @PostMapping
    public ResponseEntity<TaskListResponseDTO> createTaskList(@Valid @RequestBody TaskListRequestDTO request) {
        TaskListResponseDTO createdList = taskListService.createTaskList(request);
        return new ResponseEntity<>(createdList, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskListResponseDTO> getTaskListById(@PathVariable Long id) {
        TaskListResponseDTO list = taskListService.getTaskListById(id);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<TaskListResponseDTO>> getTaskListsByProjectId(@PathVariable Long projectId) {
        List<TaskListResponseDTO> lists = taskListService.getListsByProjectId(projectId);
        return ResponseEntity.ok(lists);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskListResponseDTO> updateTaskList(@PathVariable Long id, @Valid @RequestBody TaskListRequestDTO request) {
        TaskListResponseDTO updatedList = taskListService.updateTaskList(id, request);
        return ResponseEntity.ok(updatedList);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskList(@PathVariable Long id) {
        taskListService.deleteTaskList(id);
    }
}
