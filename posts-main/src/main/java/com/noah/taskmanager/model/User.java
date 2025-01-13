package com.noah.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.noah.taskmanager.model.enumEntity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "public")
@NamedQuery(
        name = "User.findByEmail",
        query = "SELECT u FROM User u WHERE u.email = :email"
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userid;

    @NotNull(message = "Username cannot be null.")
    @Size(min = 3, message = "Username must be at least 3 characters long.")
    @Column(nullable = false, length = 255)
    private String username;

    @NotNull(message = "Email cannot be null.")
    @Email(message = "Email should be valid.")
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @NotNull(message = "Password cannot be null.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    @Column(nullable = false, length = 255)
    private String password;

    @NotNull(message = "Role cannot be null.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<TaskEntity> tasks = new HashSet<>();

    // Default Constructor
    public User() {
        // Required for JPA
    }

    // Constructor without tasks
    public User(Long userid, String username, String email, String password, Role role) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Full Constructor
    public User(Long userid, String username, String email, String password, Role role, Set<TaskEntity> tasks) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.tasks = tasks != null ? tasks : new HashSet<>();
    }

    // PrePersist and PreUpdate for Email Normalization
    @PrePersist
    @PreUpdate
    private void normalizeEmail() {
        if (email != null) {
            email = email.trim().toLowerCase();
        }
    }

    // Getters and Setters
    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    // Overridden toString for better logging/debugging
    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
