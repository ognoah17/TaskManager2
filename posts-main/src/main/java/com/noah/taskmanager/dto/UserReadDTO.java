package com.noah.taskmanager.dto;

import com.noah.taskmanager.model.enumEntity.Role;

public class UserReadDTO {
    private Long userId;
    private String username;
    private String email;
    private Role role; // Use Role enum instead of String

    public UserReadDTO() {
        // Default constructor required by Jackson
    }

    public UserReadDTO(Long userId, String username, String email, Role role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
