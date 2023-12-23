package com.yk.unnamed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.bind.annotation.*;

import com.yk.unnamed.model.User;
import com.yk.unnamed.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        userService.register(user);
        System.out.println("register");
        return null;
        // registration logic
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        System.out.println("login");
        userService.login(user.getUsername(), user.getPassword());
        return null;
        // login logic
    }
}