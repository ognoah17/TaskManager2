package com.noah.taskmanager.service;

import com.noah.taskmanager.model.TaskEntity;
import com.noah.taskmanager.model.enumEntity.TaskPriority;
import com.noah.taskmanager.model.enumEntity.TaskStatus;
import com.noah.taskmanager.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Get tasks by user ID.
     */
    public List<TaskEntity> getTasksByUserId(Long userId) {
        return taskRepository.findByUser_Userid(userId);
    }

    /**
     * Create a new task.
     */
    public TaskEntity createTask(TaskEntity taskEntity) {
        return taskRepository.save(taskEntity);
    }

    /**
     * Update an existing task.
     */
    public TaskEntity updateTask(Long id, TaskEntity updatedTaskEntity) {
        TaskEntity existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));

        existingTask.setTaskname(updatedTaskEntity.getTaskname());
        existingTask.setDescription(updatedTaskEntity.getDescription());
        existingTask.setPriority(updatedTaskEntity.getPriority());
        existingTask.setDuedate(updatedTaskEntity.getDuedate());
        existingTask.setStatus(updatedTaskEntity.getStatus());
        existingTask.setCategories(updatedTaskEntity.getCategories());

        return taskRepository.save(existingTask);
    }

    /**
     * Delete a task by ID.
     */
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    /**
     * Get overdue tasks by status.
     */
    public List<TaskEntity> getOverdueTasks(TaskStatus status) {
        return taskRepository.findByDuedateBeforeAndStatusNot(LocalDateTime.now(), status);
    }

    /**
     * Get tasks by priority.
     */
    public List<TaskEntity> getTasksByPriority(TaskPriority priority) {
        return taskRepository.findByPriority(priority);
    }

    public TaskEntity getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + id));
    }


    /**
     * Get all tasks.
     */
    public List<TaskEntity> getAllTasks() {
        // Convert Iterable to List
        return taskRepository.findAll() instanceof List
                ? (List<TaskEntity>) taskRepository.findAll()
                : new ArrayList<>((Collection<? extends TaskEntity>) taskRepository.findAll());
    }

}
