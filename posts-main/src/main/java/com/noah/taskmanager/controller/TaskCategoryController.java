package com.noah.taskmanager.controller;

import com.noah.taskmanager.model.Category;
import com.noah.taskmanager.model.TaskCategory;
import com.noah.taskmanager.model.TaskEntity;
import com.noah.taskmanager.service.TaskCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-categories")
public class TaskCategoryController {

    private final TaskCategoryService taskCategoryService;

    public TaskCategoryController(TaskCategoryService taskCategoryService) {
        this.taskCategoryService = taskCategoryService;
    }

    @GetMapping("/task/{taskId}")
    public List<Category> getCategoriesByTaskId(@PathVariable Long taskId) {
        return taskCategoryService.getCategoriesByTaskId(taskId);
    }

    @GetMapping("/category/{categoryId}")
    public List<TaskEntity> getTasksByCategoryId(@PathVariable Long categoryId) {
        return taskCategoryService.getTasksByCategoryId(categoryId);
    }

    @PostMapping
    public TaskCategory addTaskToCategory(@RequestParam Long taskId, @RequestParam Long categoryId) {
        return taskCategoryService.addTaskToCategory(taskId, categoryId);
    }

    @DeleteMapping
    public ResponseEntity<?> removeTaskFromCategory(@RequestParam Long taskId, @RequestParam Long categoryId) {
        taskCategoryService.removeTaskFromCategory(taskId, categoryId);
        return ResponseEntity.ok().build();
    }
}
