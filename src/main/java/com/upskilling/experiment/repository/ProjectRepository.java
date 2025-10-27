/**
 * Repository interface for database operations on Project entities.
 * Extends JpaRepository to provide CRUD operations.
 * Projects serve as containers for organizing tasks and task lists.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
package com.upskilling.experiment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upskilling.experiment.entity.Project;

public interface ProjectRepository extends JpaRepository<Project,Long> {
}
