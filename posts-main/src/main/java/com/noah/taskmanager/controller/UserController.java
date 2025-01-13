package com.noah.taskmanager.controller;

import com.noah.taskmanager.dto.UserCreateDTO;
import com.noah.taskmanager.dto.UserReadDTO;
import com.noah.taskmanager.mapper.UserMapper;
import com.noah.taskmanager.model.User;
import com.noah.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{email}")
    public UserReadDTO getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return userMapper.toDTO(user);
    }

    @PostMapping
    public ResponseEntity<UserReadDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        User createdUser = userService.createUser(userCreateDTO);
        UserReadDTO userReadDTO = userMapper.toDTO(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userReadDTO);
    }


    @GetMapping("/exists/{email}")
    public boolean checkUserExists(@PathVariable String email) {
        return userService.checkUserExists(email);
    }
}
