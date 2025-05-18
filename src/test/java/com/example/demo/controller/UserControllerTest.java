package com.example.demo.controller;

import com.example.demo.exception.InvalidDataException;
import com.example.demo.model.User;
import com.example.demo.service.NotificationService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        userService = new UserService(notificationService);
        userController = new UserController(userService);
    }

    @Test
    public void registerUser_SuccessfulRegistration() {
        User userToRegister = new User(null, "testuser", "password");
        ResponseEntity<User> responseEntity = userController.registerUser(userToRegister);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void registerUser_BadRequest_NullUsername() {
        User userWithNullUsername = new User(null, null, "password");

        try {
            ResponseEntity<User> responseEntity = userController.registerUser(userWithNullUsername);
            assertTrue(true);
        } catch (InvalidDataException e) {
            assertEquals("Username cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void registerUser_BadRequest_EmptyUsername() {
        User userWithNullUsername = new User(null, "   ", "password");

        try {
            ResponseEntity<User> responseEntity = userController.registerUser(userWithNullUsername);
            assertTrue(true);
        } catch (InvalidDataException e) {
            assertEquals("Username cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void registerUser_BadRequest_NullPassword() {
        User userWithNullUsername = new User(null, "usertest", null);

        try {
            ResponseEntity<User> responseEntity = userController.registerUser(userWithNullUsername);
            assertTrue(false);
        } catch (InvalidDataException e) {
            assertEquals("Password cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void registerUser_BadRequest_EmptyPassword() {
        User userWithNullUsername = new User(null, "usertest", "");

        try {
            ResponseEntity<User> responseEntity = userController.registerUser(userWithNullUsername);
            assertTrue(true);
        } catch (InvalidDataException e) {
            assertEquals("Password cannot be empty.", e.getMessage());
        }
    }
}
