package dev.danvega.danson.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TaskCategoryId implements Serializable {

    private Long taskId;
    private Long categoryId;

    // Constructors
    public TaskCategoryId() {}

    public TaskCategoryId(Long taskId, Long categoryId) {
        this.taskId = taskId;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskCategoryId that = (TaskCategoryId) o;
        return Objects.equals(taskId, that.taskId) && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, categoryId);
    }
}
