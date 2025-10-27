/**
 * Repository interface for database operations on TaskList entities.
 * Extends JpaRepository to provide CRUD operations.
 * Task lists represent Kanban board columns within projects.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
package com.upskilling.experiment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upskilling.experiment.entity.TaskList;

public interface TaskListRepository extends JpaRepository<TaskList,Long> {

}
