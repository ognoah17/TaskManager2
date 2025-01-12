package com.noah.taskmanager.controller;

import com.noah.taskmanager.dto.TaskCreateDTO;
import com.noah.taskmanager.dto.TaskReadDTO;
import com.noah.taskmanager.dto.TaskUpdateDTO;
import com.noah.taskmanager.mapper.TaskMapper;
import com.noah.taskmanager.model.TaskEntity;
import com.noah.taskmanager.service.TaskService;
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
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public ResponseEntity<List<TaskReadDTO>> getTasks() {
        List<TaskEntity> tasks = taskService.getAllTasks();
        List<TaskReadDTO> taskDTOs = tasks.stream()
                .map(taskMapper::toDTO)
                .toList();
        return ResponseEntity.ok(taskDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskReadDTO> getTaskById(@PathVariable Long id) {
        TaskEntity task = taskService.getTaskById(id);
        TaskReadDTO taskDTO = taskMapper.toDTO(task);
        return ResponseEntity.ok(taskDTO);
    }

    @PostMapping
    public ResponseEntity<TaskReadDTO> createTask(@Valid @RequestBody TaskCreateDTO taskCreateDTO) {
        TaskEntity taskEntity = taskMapper.toEntity(taskCreateDTO);
        TaskEntity createdTask = taskService.createTask(taskEntity);
        TaskReadDTO taskReadDTO = taskMapper.toDTO(createdTask);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTask.getTaskid())
                .toUri();
        return ResponseEntity.created(location).body(taskReadDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskReadDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateDTO taskUpdateDTO) {
        TaskEntity existingTask = taskService.getTaskById(id);
        taskMapper.updateEntityFromDTO(taskUpdateDTO, existingTask);
        TaskEntity updatedTask = taskService.updateTask(id, existingTask);
        return ResponseEntity.ok(taskMapper.toDTO(updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
