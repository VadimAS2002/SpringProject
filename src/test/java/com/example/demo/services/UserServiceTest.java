package com.example.demo.services;

import com.example.demo.db.User;
import com.example.demo.db.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
    }

    @Test
    void registerUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User registeredUser = userService.registerUser(new User());

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void loginUser_Success() {
        when(userRepository.findByUsernameAndPassword("testuser", "password")).thenReturn(testUser);

        User loggedInUser = userService.loginUser("testuser", "password");

        assertNotNull(loggedInUser);
        assertEquals("testuser", loggedInUser.getUsername());
        verify(userRepository, times(1)).findByUsernameAndPassword("testuser", "password");
    }

    @Test
    void loginUser_IncorrectPassword() {
        when(userRepository.findByUsernameAndPassword("testuser", "wrongpassword")).thenReturn(null);

        User loggedInUser = userService.loginUser("testuser", "wrongpassword");

        assertNull(loggedInUser);
        verify(userRepository, times(1)).findByUsernameAndPassword("testuser", "wrongpassword");
    }

    @Test
    void loginUser_IncorrectUsername() {
        when(userRepository.findByUsernameAndPassword("wronguser", "password")).thenReturn(null);

        User loggedInUser = userService.loginUser("wronguser", "password");

        assertNull(loggedInUser);
        verify(userRepository, times(1)).findByUsernameAndPassword("wronguser", "password");
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User retrievedUser = userService.getUserById(1L);

        assertNotNull(retrievedUser);
        assertEquals("testuser", retrievedUser.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        User retrievedUser = userService.getUserById(999L);

        assertNull(retrievedUser);
        verify(userRepository, times(1)).findById(999L);
    }
}
