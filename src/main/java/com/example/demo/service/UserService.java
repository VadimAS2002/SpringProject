package com.example.demo.service;

import com.example.demo.exception.InvalidDataException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final List<User> users = new ArrayList<>();
    private static final AtomicLong userId = new AtomicLong(1);
    private final NotificationService notificationService;

    public UserService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public User registerUser(User registeredUser) {
        if (registeredUser.getUsername() == null || registeredUser.getUsername().isEmpty()) {
            throw new InvalidDataException("Username cannot be empty.");
        }

        if (registeredUser.getPassword() == null || registeredUser.getPassword().isEmpty()) {
            throw new InvalidDataException("Password cannot be empty.");
        }
        registeredUser.setId(userId.getAndIncrement());
        users.add(registeredUser);
        notificationService.createNotification(registeredUser, "User " + registeredUser.getUsername() + " registered!");
        return registeredUser;
    }

    public Optional<User> login(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public User getUserById(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst().orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));
    }
}
