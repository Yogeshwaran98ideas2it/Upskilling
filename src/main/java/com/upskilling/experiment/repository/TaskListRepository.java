package com.upskilling.experiment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upskilling.experiment.entity.TaskList;

public interface TaskListRepository extends JpaRepository<TaskList,Long> {

}
