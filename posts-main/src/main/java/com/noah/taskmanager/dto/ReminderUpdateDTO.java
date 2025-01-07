package com.noah.taskmanager.dto;

import java.time.LocalDateTime;

public class ReminderUpdateDTO {
    private LocalDateTime reminderTime;

    // Getters and Setters
    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }
}
