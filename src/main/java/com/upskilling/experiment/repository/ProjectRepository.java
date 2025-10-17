package com.upskilling.experiment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upskilling.experiment.entity.Project;

public interface ProjectRepository extends JpaRepository<Project,Long> {
}
