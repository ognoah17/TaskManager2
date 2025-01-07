package com.noah.taskmanager.mapper;

import com.noah.taskmanager.dto.*;
import com.noah.taskmanager.model.*;

public class NotificationMapper {

    public Notification toEntity(NotificationCreateDTO dto) {
        Notification notification = new Notification();
        notification.setNotificationType(dto.getNotificationType());
        notification.setNotificationTime(dto.getNotificationTime());
        return notification;
    }

    public NotificationReadDTO toDTO(Notification notification) {
        return new NotificationReadDTO(
                notification.getNotificationid(),
                notification.getNotificationType(),
                notification.getNotificationTime(),
                notification.getTask().getTaskid(),
                notification.getUser().getUserid()
        );
    }
}
