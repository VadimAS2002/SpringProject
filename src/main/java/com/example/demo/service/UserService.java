package com.example.demo.service;

import com.example.demo.exception.InvalidDataException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public UserService(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public User registerUser(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new InvalidDataException("Username cannot be empty.");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new InvalidDataException("Password cannot be empty.");
        }
        User registeredUser = userRepository.createUser(user);
        notificationService.createNotification(registeredUser, "User " + user.getUsername() + " registered!");
        return userRepository.createUser(user);
    }

    public User login(String username) {
        return userRepository.findUserByUsername(username).orElse(null);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));
    }
}
