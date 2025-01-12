package com.noah.taskmanager.dto;

import java.time.LocalDateTime;

public class TaskCreateDTO {
    private String taskname;
    private String description;
    private String priority;
    private LocalDateTime dueDate;
    private Long userId;

    // Getters and Setters
    public String getTaskName() {
        return taskname;
    }

    public void setTaskName(String taskname) {
        this.taskname = taskname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
