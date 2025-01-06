package dev.danvega.danson.repository;

import dev.danvega.danson.model.TaskEntity;
import dev.danvega.danson.model.enumEntity.TaskPriority;
import dev.danvega.danson.model.enumEntity.TaskStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<TaskEntity, Long> {

    // Find tasks by priority
    List<TaskEntity> findByPriority(TaskPriority priority);

    // Find tasks by status
    List<TaskEntity> findByStatus(TaskStatus status);

    // Find tasks by user ID
    List<TaskEntity> findByUser_Userid(Long userId);

    // Find overdue tasks
    List<TaskEntity> findByDuedateBeforeAndStatusNot(LocalDateTime dueDate, TaskStatus completedStatus);
}
