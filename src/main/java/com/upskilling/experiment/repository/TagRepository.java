// TagRepository.java
package com.upskilling.experiment.repository;

import com.upskilling.experiment.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {}