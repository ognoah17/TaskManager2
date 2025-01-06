package dev.danvega.danson.service;

import dev.danvega.danson.model.Notification;
import dev.danvega.danson.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUser_Userid(userId);
    }

    public List<Notification> getNotificationsByTaskId(Long taskId) {
        return notificationRepository.findByTask_Taskid(taskId);
    }

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
