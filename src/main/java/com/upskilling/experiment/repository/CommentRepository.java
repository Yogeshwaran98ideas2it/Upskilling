// CommentRepository.java
package com.upskilling.experiment.repository;

import com.upskilling.experiment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {}