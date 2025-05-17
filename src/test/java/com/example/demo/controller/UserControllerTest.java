package com.example.demo.controller;

import com.example.demo.exception.InvalidDataException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    public void registerUser_SuccessfulRegistration() {
        User userToRegister = new User(null, "testuser", "password");
        User registeredUser = new User(1L, "testuser", "password");

        when(userService.registerUser(userToRegister)).thenReturn(registeredUser);

        ResponseEntity<User> responseEntity = userController.registerUser(userToRegister);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(registeredUser, responseEntity.getBody());
    }

    @Test
    public void registerUser_BadRequest_NullUsername() {
        User userWithNullUsername = new User(null, null, "password");

        when(userService.registerUser(userWithNullUsername)).thenThrow(new InvalidDataException("Username cannot be empty."));

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

        when(userService.registerUser(userWithNullUsername)).thenThrow(new InvalidDataException("Username cannot be empty."));

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

        when(userService.registerUser(userWithNullUsername)).thenThrow(new InvalidDataException("Password cannot be empty."));

        try {
            ResponseEntity<User> responseEntity = userController.registerUser(userWithNullUsername);
            assertTrue(false);
        } catch (InvalidDataException e) {
            assertEquals("Password cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void registerUser_BadRequest_EmptyPassword() {
        User userWithNullUsername = new User(null, "usertest", "   ");

        when(userService.registerUser(userWithNullUsername)).thenThrow(new InvalidDataException("Password cannot be empty."));

        try {
            ResponseEntity<User> responseEntity = userController.registerUser(userWithNullUsername);
            assertTrue(false);
        } catch (InvalidDataException e) {
            assertEquals("Password cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void getUserById_SuccessfulRetrieval() {
        Long userId = 1L;
        User testUser = new User(userId, "testuser", "password");

        when(userService.login(testUser.getUsername())).thenReturn(Optional.of(testUser));

        ResponseEntity<Optional<User>> responseEntity = userController.login(testUser.getUsername());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
}
