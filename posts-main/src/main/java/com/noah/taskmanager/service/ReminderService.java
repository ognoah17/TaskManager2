package com.noah.taskmanager.service;

import com.noah.taskmanager.model.Reminder;
import com.noah.taskmanager.repository.ReminderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public List<Reminder> getRemindersByUserId(Long userId) {
        return reminderRepository.findByUser_Userid(userId);
    }

    public Reminder createReminder(Reminder reminder) {
        if (reminder.getReminderTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reminder time must be in the future.");
        }
        return reminderRepository.save(reminder);
    }

    public void deleteReminder(Long id) {
        reminderRepository.deleteById(id);
    }
}
