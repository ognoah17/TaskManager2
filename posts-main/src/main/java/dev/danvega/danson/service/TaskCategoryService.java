package dev.danvega.danson.service;

import dev.danvega.danson.model.Category;
import dev.danvega.danson.model.TaskCategory;
import dev.danvega.danson.model.TaskCategoryId;
import dev.danvega.danson.model.TaskEntity;
import dev.danvega.danson.repository.CategoryRepository;
import dev.danvega.danson.repository.TaskCategoryRepository;
import dev.danvega.danson.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskCategoryService {

    private final TaskCategoryRepository taskCategoryRepository;
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    public TaskCategoryService(TaskCategoryRepository taskCategoryRepository, TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskCategoryRepository = taskCategoryRepository;
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    // Find all TaskCategory relationships for a specific task
    public List<TaskCategory> getTaskCategoriesByTaskId(Long taskId) {
        return taskCategoryRepository.findByTask_Taskid(taskId);
    }

    // Find all TaskCategory relationships for a specific category
    public List<TaskCategory> getTaskCategoriesByCategoryId(Long categoryId) {
        return taskCategoryRepository.findByCategory_CategoryId(categoryId);
    }

    // Find all tasks associated with a specific category
    public List<TaskEntity> getTasksByCategoryId(Long categoryId) {
        return taskCategoryRepository.findTasksByCategoryId(categoryId);
    }

    // Find all categories associated with a specific task
    public List<Category> getCategoriesByTaskId(Long taskId) {
        return taskCategoryRepository.findCategoriesByTaskId(taskId);
    }

    // Associate a task with a category
    public TaskCategory addTaskToCategory(Long taskId, Long categoryId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        TaskCategoryId id = new TaskCategoryId(taskId, categoryId);
        if (taskCategoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Task is already associated with the category.");
        }

        TaskCategory taskCategory = new TaskCategory(task, category);
        return taskCategoryRepository.save(taskCategory);
    }

    // Remove a task from a category
    public void removeTaskFromCategory(Long taskId, Long categoryId) {
        TaskCategoryId id = new TaskCategoryId(taskId, categoryId);
        TaskCategory taskCategory = taskCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task-Category association not found for taskId: " + taskId + " and categoryId: " + categoryId));
        taskCategoryRepository.delete(taskCategory);
    }
}
