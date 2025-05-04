package com.example.demo.services;

import com.example.demo.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void registerUser_Success() {
        User user = new User(null, "testuser", "password");

        User registeredUser = userService.registerUser(user);

        assertNotNull(registeredUser.getId());
        assertEquals("testuser", registeredUser.getUsername());
        assertEquals("password", registeredUser.getPassword());
    }

    @Test
    void loginUser_Success() {
        User user = new User(null, "testuser", "password");
        userService.registerUser(user);

        User loggedInUser = userService.loginUser("testuser", "password");

        assertNotNull(loggedInUser);
        assertEquals("testuser", loggedInUser.getUsername());
    }

    @Test
    void loginUser_IncorrectPassword() {
        User user = new User(null, "testuser", "password");
        userService.registerUser(user);

        User loggedInUser = userService.loginUser("testuser", "wrongpassword");

        assertNull(loggedInUser);
    }

    @Test
    void loginUser_IncorrectUsername() {
        User user = new User(null, "testuser", "password");
        userService.registerUser(user);

        User loggedInUser = userService.loginUser("wronguser", "password");

        assertNull(loggedInUser);
    }

    @Test
    void getUserById_Success() {
        User user = new User(null, "testuser", "password");
        User registeredUser = userService.registerUser(user);
        Long userId = registeredUser.getId();

        User retrievedUser = userService.getUserById(userId);

        assertNotNull(retrievedUser);
        assertEquals("testuser", retrievedUser.getUsername());
    }

    @Test
    void getUserById_NotFound() {
        Long nonExistentUserId = 999L;

        User retrievedUser = userService.getUserById(nonExistentUserId);

        assertNull(retrievedUser);
    }
}
