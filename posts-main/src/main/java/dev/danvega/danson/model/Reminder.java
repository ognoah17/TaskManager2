package dev.danvega.danson.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminders", schema = "public",
        indexes = {
                @Index(name = "idx_reminder_time", columnList = "reminderTime")
        }
)
@NamedQueries({
        @NamedQuery(
                name = "Reminder.findByUser",
                query = "SELECT r FROM Reminder r WHERE r.user.userid = :userId"
        ),
        @NamedQuery(
                name = "Reminder.findByTask",
                query = "SELECT r FROM Reminder r WHERE r.task.taskid = :taskId"
        )
})
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reminderid;

    @NotNull(message = "Reminder time cannot be null.")
    @Future(message = "Reminder time must be in the future.")
    @Column(nullable = false)
    @JsonProperty("reminderTime") // Ensure this field is serialized
    private LocalDateTime reminderTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private TaskEntity task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public Reminder() {}

    public Reminder(LocalDateTime reminderTime, TaskEntity task, User user) {
        this.reminderTime = reminderTime;
        this.task = task;
        this.user = user;
    }

    // Lifecycle Callbacks
    @PrePersist
    void validateReminderTime() {
        if (reminderTime == null || reminderTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reminder time must be in the future.");
        }
    }

    // Getters and Setters
    public Long getReminderid() {
        return reminderid;
    }

    public void setReminderid(Long reminderid) {
        this.reminderid = reminderid;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
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
        return "Reminder{" +
                "reminderid=" + reminderid +
                ", reminderTime=" + reminderTime +
                ", task=" + (task != null ? task.getTaskid() : null) +
                ", user=" + (user != null ? user.getUserid() : null) +
                '}';
    }
}
