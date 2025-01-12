package com.noah.taskmanager.dto;

import java.time.LocalDateTime;

public class TaskUpdateDTO {
    private String taskname;
    private String description;
    private String priority;
    private String status;
    private LocalDateTime dueDate;

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
}
