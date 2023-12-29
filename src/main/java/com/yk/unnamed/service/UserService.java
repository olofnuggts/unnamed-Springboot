package com.yk.unnamed.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yk.unnamed.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

}