package com.upskilling.experiment.service.impl;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

import com.upskilling.experiment.dto.response.ActivityLogResponseDTO;
import com.upskilling.experiment.entity.ActivityLog;
import com.upskilling.experiment.entity.Project;
import com.upskilling.experiment.entity.Task;
import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.mapper.ActivityLogMapper;
import com.upskilling.experiment.repository.ActivityLogRepository;
import com.upskilling.experiment.service.ActivityLogService;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final ActivityLogMapper activityLogMapper;

    public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository, ActivityLogMapper activityLogMapper) {
        this.activityLogRepository = activityLogRepository;
        this.activityLogMapper = activityLogMapper;
    }


    @Override
    public ActivityLogResponseDTO createLog(String type, String details, User user, Task task, Project project) {
        ActivityLog log = new ActivityLog();
        log.setActivityType(type);
        log.setDetails(details);
        log.setUser(user);
        log.setTask(task);
        log.setProject(project);

        ActivityLog savedLog = activityLogRepository.save(log);

        // Map the saved entity to a DTO before returning
        return activityLogMapper.toDto(savedLog);
    }

    @Override
    public List<ActivityLogResponseDTO> getRecentLogs(int limit) {
        // Use Spring Data JPA Paging/Sorting for efficiency
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "timestamp"));
        List<ActivityLog> logs = activityLogRepository.findAll(pageRequest).getContent();

        return activityLogMapper.toDtoList(logs);
    }
}