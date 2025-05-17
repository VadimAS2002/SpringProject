package com.example.demo.service;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    private User testUser;

    @Autowired
    private  NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService();
        userService = new UserService(notificationService);
    }

    @Test
    void registerUserTest() {
        User testUser = new User(1L, "testuser", "password");
        User registeredUser = userService.registerUser(testUser);

        assertEquals("testuser", registeredUser.getUsername());
        assertEquals("password", registeredUser.getPassword());
    }

    @Test
    void loginNotFoundTest() {
        Optional<User> loggedInUser = userService.login("nonexistentuser");
        assertTrue(loggedInUser.isEmpty());
    }

    @Test
    void findUserById() {
        User testUser2 = new User(null, "testuser2", "password2");
        User registeredUser = userService.registerUser(testUser2);

        User findUser = userService.getUserById(registeredUser.getId());

        assertEquals("testuser2", findUser.getUsername());
        assertEquals("password2", findUser.getPassword());
    }

    @Test
    void findUserById_WrongId() {
        Long userId = 9999L;

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(userId);
        });
    }

    @Test
    void findUserByUsername() {
        User testUser2 = new User(null, "testuser3", "password3");
        userService.registerUser(testUser2);

        Optional<User> user = userService.login("testuser3");

        assertEquals("testuser3", user.get().getUsername());
        assertEquals("password3", user.get().getPassword());
    }
}
