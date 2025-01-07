package com.noah.taskmanager.mapper;

import com.noah.taskmanager.dto.*;
import com.noah.taskmanager.model.*;

public class ReminderMapper {

    public Reminder toEntity(ReminderCreateDTO dto) {
        Reminder reminder = new Reminder();
        reminder.setReminderTime(dto.getReminderTime());
        return reminder;
    }

    public ReminderReadDTO toDTO(Reminder reminder) {
        return new ReminderReadDTO(
                reminder.getReminderid(),
                reminder.getReminderTime(),
                reminder.getTask().getTaskid(),
                reminder.getUser().getUserid()
        );
    }

    public void updateEntityFromDTO(ReminderUpdateDTO dto, Reminder reminder) {
        if (dto.getReminderTime() != null) reminder.setReminderTime(dto.getReminderTime());
    }
}
