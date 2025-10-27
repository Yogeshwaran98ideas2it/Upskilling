/**
 * Repository interface for database operations on Tag entities.
 * Extends JpaRepository to provide CRUD operations.
 * Tags are reusable categorization labels for tasks.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
package com.upskilling.experiment.repository;

import com.upskilling.experiment.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {}