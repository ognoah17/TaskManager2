package com.noah.taskmanager.controller;

import com.noah.taskmanager.model.Reminder;
import com.noah.taskmanager.service.ReminderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping("/user/{userId}")
    public List<Reminder> getRemindersByUserId(@PathVariable Long userId) {
        return reminderService.getRemindersByUserId(userId);
    }

    @PostMapping
    public Reminder createReminder(@RequestBody Reminder reminder) {
        return reminderService.createReminder(reminder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReminder(@PathVariable Long id) {
        reminderService.deleteReminder(id);
        return ResponseEntity.ok().build();
    }
}
