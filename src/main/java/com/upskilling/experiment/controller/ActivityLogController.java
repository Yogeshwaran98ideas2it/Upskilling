package com.upskilling.experiment.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upskilling.experiment.dto.response.ActivityLogResponseDTO;
import com.upskilling.experiment.service.ActivityLogService;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @GetMapping("/recent")
    public ResponseEntity<List<ActivityLogResponseDTO>> getRecentActivityLogs(@RequestParam(defaultValue = "20") int limit) {
        List<ActivityLogResponseDTO> logs = activityLogService.getRecentLogs(limit);
        return ResponseEntity.ok(logs);
    }
}
