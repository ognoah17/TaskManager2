package com.noah.taskmanager.repository;

import com.noah.taskmanager.model.User;
import com.noah.taskmanager.model.enumEntity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by email
    Optional<User> findByEmail(String email);

    // Check if a user exists by email
    boolean existsByEmail(String email);

    // Find all users with a specific role
    List<User> findByRole(Role role);
}
