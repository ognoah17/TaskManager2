package com.noah.taskmanager.dto;

import java.time.LocalDateTime;

public class NotificationReadDTO {
    private Long notificationId;
    private String notificationType;
    private LocalDateTime notificationTime;
    private Long taskId; // Added
    private Long userId; // Added

    // Updated Constructor
    public NotificationReadDTO(Long notificationId, String notificationType, LocalDateTime notificationTime, Long taskId, Long userId) {
        this.notificationId = notificationId;
        this.notificationType = notificationType;
        this.notificationTime = notificationTime;
        this.taskId = taskId;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public LocalDateTime getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(LocalDateTime notificationTime) {
        this.notificationTime = notificationTime;
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
