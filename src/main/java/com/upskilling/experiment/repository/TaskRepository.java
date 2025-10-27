/**
 * Repository interface for database operations on Task entities.
 * Extends JpaRepository to provide CRUD operations.
 * Can be extended with custom query methods for task filtering and searching.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
package com.upskilling.experiment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.upskilling.experiment.entity.Task;
import com.upskilling.experiment.enums.TaskStatus;
import com.upskilling.experiment.enums.TaskType;
import com.upskilling.experiment.enums.TicketType;

public interface TaskRepository extends JpaRepository<Task, Long> {

//    List<Task> findByProjectId(Long projectId);
//    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);
//    List<Task> findByProjectIdAndAssigneeId(Long projectId, Long assigneeId);
//    List<Task> findByProjectIdAndStatusAndAssigneeId(Long projectId, TaskStatus status, Long assigneeId);
//
//    List<Task> findByProjectIdAndTicketType(Long projectId, TicketType ticketType);
//    List<Task> findByProjectIdAndTicketTypeAndTaskType(Long projectId, TicketType ticketType, TaskType taskType);
}
// Add standard JpaRepository interfaces for: UserRepository, ProjectRepository, TaskListRepository, TagRepository, CommentRepository, ActivityLogRepository