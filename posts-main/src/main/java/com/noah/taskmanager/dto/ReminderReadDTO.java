package com.noah.taskmanager.dto;

import java.time.LocalDateTime;

public class ReminderReadDTO {
    private Long reminderId;
    private LocalDateTime reminderTime;
    private Long taskId; // Reference task by ID
    private Long userId; // Reference user by ID

    // Constructor
    public ReminderReadDTO(Long reminderId, LocalDateTime reminderTime, Long taskId, Long userId) {
        this.reminderId = reminderId;
        this.reminderTime = reminderTime;
        this.taskId = taskId;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getReminderId() {
        return reminderId;
    }

    public void setReminderId(Long reminderId) {
        this.reminderId = reminderId;
    }

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
