package com.upskilling.experiment.service;

import com.upskilling.experiment.entity.Task;
import com.upskilling.experiment.entity.User;

public interface NotificationService {

     void notifyTaskAssignment(User assignee, Task task) ;

    void notifyTaskStatusChange(Task task, String oldStatus, String newStatus);

}
