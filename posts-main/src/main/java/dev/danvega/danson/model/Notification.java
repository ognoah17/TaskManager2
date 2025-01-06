package dev.danvega.danson.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications", schema = "public",
        indexes = {
                @Index(name = "idx_notification_time", columnList = "notificationTime")
        }
)
@NamedQueries({
        @NamedQuery(
                name = "Notification.findByUser",
                query = "SELECT n FROM Notification n WHERE n.user.userid = :userId"
        ),
        @NamedQuery(
                name = "Notification.findByTask",
                query = "SELECT n FROM Notification n WHERE n.task.taskid = :taskId"
        )
})
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationid;

    @NotNull(message = "Notification type cannot be null.")
    @Size(min = 1, max = 50, message = "Notification type must be between 1 and 50 characters.")
    @Column(nullable = false)
    private String notificationType;

    @NotNull(message = "Notification time cannot be null.")
    @FutureOrPresent(message = "Notification time must be in the future or present.")
    @Column(nullable = false)
    private LocalDateTime notificationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private TaskEntity task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Notification() {}

    public Notification(String notificationType, LocalDateTime notificationTime, TaskEntity task, User user) {
        this.notificationType = notificationType;
        this.notificationTime = notificationTime;
        this.task = task;
        this.user = user;
    }

    @PrePersist
    void validateNotificationTime() {
        if (notificationTime == null || notificationTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Notification time must be in the future or present.");
        }
    }

    public Long getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(Long notificationid) {
        this.notificationid = notificationid;
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

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationid=" + notificationid +
                ", notificationType='" + notificationType + '\'' +
                ", notificationTime=" + notificationTime +
                '}';
    }
}
