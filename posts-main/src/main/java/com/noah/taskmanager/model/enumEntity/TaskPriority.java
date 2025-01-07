package com.noah.taskmanager.model.enumEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TaskPriority {
    @JsonProperty("LOW")
    LOW,
    @JsonProperty("MEDIUM")
    MEDIUM,
    @JsonProperty("HIGH")
    HIGH
}
