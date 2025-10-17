package com.upskilling.experiment.service;

import java.util.List;

import com.upskilling.experiment.dto.response.ActivityLogResponseDTO;
import com.upskilling.experiment.entity.Project;
import com.upskilling.experiment.entity.Task;
import com.upskilling.experiment.entity.User;

public interface ActivityLogService {

    ActivityLogResponseDTO createLog(String type, String details, User user, Task task, Project project);
    List<ActivityLogResponseDTO> getRecentLogs(int limit);
}
