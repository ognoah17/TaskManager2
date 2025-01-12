package com.noah.taskmanager.dto;

import java.time.LocalDateTime;

public class TaskReadDTO {
    private Long taskId;
    private String taskname;
    private String description;
    private String priority;
    private String status;
    private LocalDateTime dueDate;
    private Long userId;

    public TaskReadDTO() {}

    public TaskReadDTO(Long taskId, String taskname, String description, String priority, String status, LocalDateTime dueDate, Long userId) {
        this.taskId = taskId;
        this.taskname = taskname;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        this.userId = userId;
    }


    // Getters and Setters
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
