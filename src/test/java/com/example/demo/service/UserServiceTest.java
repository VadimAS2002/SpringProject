package com.example.demo.service;

import com.example.demo.exception.InvalidDataException;
import com.example.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService();
        userService = new UserService(notificationService);
    }

    @Test
    void registerUser_ValidInput_ReturnsRegisteredUser() {
        User user = new User(1L, "testUser", "password");

        User registeredUser = userService.registerUser(user);

        assertNotNull(registeredUser.getId());
        assertEquals("testUser", registeredUser.getUsername());
        assertEquals("password", registeredUser.getPassword());
    }

    @Test
    void registerUser_EmptyUsername() {
        User user = new User(1L, "", "password");
        assertThrows(InvalidDataException.class, () -> userService.registerUser(user));
    }

    @Test
    void registerUser_EmptyPassword() {
        User user = new User(1L, "testUser", "");
        assertThrows(InvalidDataException.class, () -> userService.registerUser(user));
    }

    @Test
    void findUserById() {
        User user = new User(1L, "testUser", "password");
        User registeredUser = userService.registerUser(user);

        User retrievedUser = userService.getUserById(registeredUser.getId());

        assertEquals(registeredUser.getId(), retrievedUser.getId());
        assertEquals(registeredUser.getUsername(), retrievedUser.getUsername());
        assertEquals(registeredUser.getPassword(), retrievedUser.getPassword());
    }

    @Test
    void findUserById_WrongId() {
        assertThrows(Exception.class, () -> userService.getUserById(999L));
    }
}
