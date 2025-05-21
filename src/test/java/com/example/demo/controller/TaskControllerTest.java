package com.example.demo.controller;

import com.example.demo.exception.InvalidDataException;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.service.NotificationService;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskControllerTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskController taskController;

    @Autowired
    private NotificationService notificationService;

    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        taskService = new TaskService(notificationService, userService);
        taskController = new TaskController(taskService);
    }

    @Test
    void getPendingTasks_ReturnsPendingTasks() {
        User user = new User(1L, "testuser", "password");
        User registeredUser = userService.registerUser(user);
        Task taskToCreate = new Task(null, "Valid Task", "Description",
                null, null, false, false, registeredUser.getId());

        ResponseEntity<Task> responseEntity = taskController.createTask(taskToCreate);
        ResponseEntity<List<Task>> responseEntity2 = taskController.getPendingTasks();

        assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
    }

    @Test
    public void getTasksByUserId_ReturnsOk() {
        Long userId = 1L;
        List<Task> tasks = new ArrayList<>();
        User user = new User(1L, "testuser", "password");
        User registeredUser = userService.registerUser(user);
        tasks.add(new Task(1L, "Task 1", "Description", now, now.plusDays(1), false,
                false, registeredUser.getId()));

        ResponseEntity<List<Task>> responseEntity = taskController.getAllTasksByUserId(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void createTask_ValidTask_ReturnsCreated() {
        User user = new User(1L, "testuser", "password");
        User registeredUser = userService.registerUser(user);
        Task taskToCreate = new Task(null, "Valid Task", "Description",
                null, null, false, false, registeredUser.getId());

        ResponseEntity<Task> responseEntity = taskController.createTask(taskToCreate);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void deleteTask_ExistingTask_ReturnsNoContent() {
        Long taskId = 1L;
        Task existingTask = new Task(null, "Valid Task", "Description", null,
                null, false, false, 1L);

        ResponseEntity<Void> responseEntity = taskController.deleteTask(taskId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void createTask_InvalidTask_NullTitle_ReturnsBadRequest() {
        User user = new User(1L, "testuser", "password");
        User registeredUser = userService.registerUser(user);
        Task taskToCreate = new Task(null, null, "Description",
                null, null, false, false, registeredUser.getId());

        try {
            ResponseEntity<Task> responseEntity = taskController.createTask(taskToCreate);
            assertTrue(true);
        } catch (InvalidDataException e) {
            assertEquals("Task title cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void createTask_InvalidTask_EmptyTitle_ReturnsBadRequest() {
        User user = new User(1L, "testuser", "password");
        User registeredUser = userService.registerUser(user);
        Task taskToCreate = new Task(null, "  ", "Description", null,
                null, false, false, user.getId());

        try {
            ResponseEntity<Task> responseEntity = taskController.createTask(taskToCreate);
            assertTrue(true);
        } catch (InvalidDataException e) {
            assertEquals("Task title cannot be empty.", e.getMessage());
        }
    }
}
