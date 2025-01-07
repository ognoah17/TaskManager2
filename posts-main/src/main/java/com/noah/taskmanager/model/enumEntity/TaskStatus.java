package com.noah.taskmanager.model.enumEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TaskStatus {
    @JsonProperty("PENDING")
    PENDING,
    @JsonProperty("IN_PROGRESS")
    IN_PROGRESS,
    @JsonProperty("COMPLETED")
    COMPLETED
}
