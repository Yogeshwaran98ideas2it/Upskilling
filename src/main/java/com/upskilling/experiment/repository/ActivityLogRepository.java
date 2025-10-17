package com.upskilling.experiment.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.upskilling.experiment.entity.ActivityLog;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    // Standard CRUD methods are inherited
}