package com.example.demo.service;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.InMemoryImpl.InMemoryUserRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
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
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(notificationRepository);
        userRepository = new InMemoryUserRepository();
        userService = new UserService(notificationService, userRepository);
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
        Optional<User> loggedInUser = Optional.ofNullable(userService.login("nonexistentuser"));
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

        Optional<User> user = Optional.ofNullable(userService.login("testuser3"));

        assertEquals("testuser3", user.get().getUsername());
        assertEquals("password3", user.get().getPassword());
    }
}
