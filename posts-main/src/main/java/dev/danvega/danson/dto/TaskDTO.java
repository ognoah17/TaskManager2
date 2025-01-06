package dev.danvega.danson.dto;

import dev.danvega.danson.model.enumEntity.TaskPriority;
import dev.danvega.danson.model.enumEntity.TaskStatus;

import java.time.LocalDateTime;
import java.util.Set;

public class TaskDTO {

    private Long taskId;  // ID of the task
    private String taskName;  // Name of the task
    private String description;  // Task description
    private TaskPriority priority;  // Priority of the task
    private LocalDateTime dueDate;  // Due date of the task
    private TaskStatus status;  // Status of the task
    private Set<Long> categoryIds;  // IDs of associated categories
    private Long userId;  // ID of the user assigned to this task

    // Getters and Setters
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Set<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                ", status=" + status +
                ", categoryIds=" + categoryIds +
                ", userId=" + userId +
                '}';
    }
}
