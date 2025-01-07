package com.noah.taskmanager.dto;

import java.time.LocalDateTime;

public class ReminderCreateDTO {
    private LocalDateTime reminderTime;
    private Long taskId; // Associate with a task
    private Long userId; // Optionally associate with a user

    // Getters and Setters
    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
