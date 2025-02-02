package com.noah.taskmanager.mapper;

import com.noah.taskmanager.dto.UserCreateDTO;
import com.noah.taskmanager.dto.UserReadDTO;
import com.noah.taskmanager.dto.UserUpdateDTO;
import com.noah.taskmanager.model.User;
import com.noah.taskmanager.model.enumEntity.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserCreateDTO dto) {
        return new User(
                null, // ID will be auto-generated
                dto.getUsername(), // Map username
                dto.getEmail(),
                dto.getPassword(),
                Role.valueOf(dto.getRole()), // Convert String to Role enum
                null // Tasks will be managed separately
        );
    }

    public UserReadDTO toDTO(User user) {
        return new UserReadDTO(
                user.getUserid(),
                user.getUsername(),
                user.getEmail(),
                user.getRole() // Map Role enum directly
        );
    }

    public void updateEntityFromDTO(UserUpdateDTO dto, User user) {
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getRole() != null) user.setRole(Role.valueOf(dto.getRole())); // Convert String to Role enum
    }
}
