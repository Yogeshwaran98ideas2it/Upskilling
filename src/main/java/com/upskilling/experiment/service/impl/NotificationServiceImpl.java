package com.upskilling.experiment.service.impl;


import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.upskilling.experiment.entity.Task;
import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.service.NotificationService;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    public void notifyTaskAssignment(User assignee, Task task) {
        log.info("NOTIFICATION SENT: Task '{}' assigned to user {} ({}).",
                task.getTitle(), assignee.getUsername(), assignee.getEmail());
    }

    @Override
    public void notifyTaskStatusChange(Task task, String oldStatus, String newStatus) {
        log.info("NOTIFICATION SENT: Status of Task '{}' changed from {} to {}.",
                task.getTitle(), oldStatus, newStatus);
    }
}