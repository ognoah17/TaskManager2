package com.noah.taskmanager.controller;

import com.noah.taskmanager.model.User;
import com.noah.taskmanager.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/exists/{email}")
    public boolean checkUserExists(@PathVariable String email) {
        return userService.checkUserExists(email);
    }
}
