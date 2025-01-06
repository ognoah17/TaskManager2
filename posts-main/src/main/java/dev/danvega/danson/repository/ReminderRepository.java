package dev.danvega.danson.repository;

import dev.danvega.danson.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    // Find reminders by user ID
    List<Reminder> findByUser_Userid(Long userId);

    // Find reminders by task ID
    List<Reminder> findByTask_Taskid(Long taskId);

    // Find all reminders scheduled for a specific time or later
    List<Reminder> findByReminderTimeAfter(LocalDateTime time);

    // Find reminders scheduled between two times
    List<Reminder> findByReminderTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
