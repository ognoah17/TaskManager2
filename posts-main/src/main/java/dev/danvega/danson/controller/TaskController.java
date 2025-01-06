package dev.danvega.danson.controller;

import dev.danvega.danson.model.TaskEntity;
import dev.danvega.danson.model.enumEntity.TaskPriority;
import dev.danvega.danson.model.enumEntity.TaskStatus;
import dev.danvega.danson.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Get all tasks.
     */
    @GetMapping
    public ResponseEntity<List<TaskEntity>> getTasks() {
        List<TaskEntity> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get tasks for a specific user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskEntity>> getTasksByUserId(@PathVariable Long userId) {
        List<TaskEntity> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Create a new task.
     */
    @PostMapping
    public ResponseEntity<TaskEntity> createTask(@Valid @RequestBody TaskEntity taskEntity) {
        TaskEntity createdTask = taskService.createTask(taskEntity);
        System.out.println("Task created: " + createdTask); // Debug logging
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTask.getTaskid())
                .toUri();
        System.out.println("Location URI: " + location); // Debug logging
        return ResponseEntity.created(location).body(createdTask);
    }


    /**
     * Update an existing task.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskEntity> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskEntity taskEntity
    ) {
        // Delegate the update logic to the service
        TaskEntity updatedTask = taskService.updateTask(id, taskEntity);

        // Return the updated task with status 200 OK
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Delete a task by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get overdue tasks by status.
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<TaskEntity>> getOverdueTasks(@RequestParam TaskStatus status) {
        List<TaskEntity> overdueTasks = taskService.getOverdueTasks(status);
        return ResponseEntity.ok(overdueTasks);
    }

    /**
     * Get tasks by priority.
     */
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskEntity>> getTasksByPriority(@PathVariable TaskPriority priority) {
        List<TaskEntity> tasks = taskService.getTasksByPriority(priority);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get a task by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskEntity> getTaskById(@PathVariable Long id) {
        TaskEntity task = taskService.getTaskById(id);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }
}
