package dev.danvega.danson.repository;

import dev.danvega.danson.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Find notifications by user ID
    List<Notification> findByUser_Userid(Long userId);

    // Find notifications by task ID
    List<Notification> findByTask_Taskid(Long taskId);

    // Find notifications by type
    List<Notification> findByNotificationType(String notificationType);

    // Find notifications scheduled for or after a specific time
    List<Notification> findByNotificationTimeAfter(LocalDateTime time);

    // Find notifications between two specific times
    List<Notification> findByNotificationTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
