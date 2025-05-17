package com.example.demo.controller;

import com.example.demo.exception.InvalidDataException;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskController taskController;

    private final LocalDateTime now = LocalDateTime.now();

    @Test
    void getPendingTasks_ReturnsPendingTasks() {
        List<Task> pendingTasks = new ArrayList<>();
        User user = new User(1L, "testuser", "password");
        pendingTasks.add(new Task(1L, "Task 1", "Description",
                now, now.plusDays(1), false, false, user));
        when(taskService.getPendingTasks()).thenReturn(pendingTasks);

        ResponseEntity<List<Task>> responseEntity = taskController.getPendingTasks();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pendingTasks, responseEntity.getBody());
    }

    @Test
    public void getTasksByUserId_ReturnsOk() {
        Long userId = 1L;
        List<Task> tasks = new ArrayList<>();
        User user = new User(1L, "testuser", "password");
        tasks.add(new Task(1L, "Task 1", "Description", now, now.plusDays(1), false,
                false, user));
        when(taskService.getAllTasksByUserId(userId)).thenReturn(tasks);

        ResponseEntity<List<Task>> responseEntity = taskController.getAllTasksByUserId(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(tasks, responseEntity.getBody());
    }

    @Test
    public void createTask_ValidTask_ReturnsCreated() {
        User user = new User(1L, "testuser", "password");
        Task taskToCreate = new Task(null, "Valid Task", "Description",
                null, null, false, false, user);
        Task createdTask = new Task(1L, "Valid Task", "Description",
                now, now.plusDays(1), false, false, user);

        when(userService.getUserById(user.getId())).thenReturn(user);
        when(taskService.createTask(taskToCreate)).thenReturn(createdTask);

        ResponseEntity<Task> responseEntity = taskController.createTask(taskToCreate);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(createdTask, responseEntity.getBody());
    }

    @Test
    public void deleteTask_ExistingTask_ReturnsNoContent() {
        Long taskId = 1L;
        Task existingTask = new Task(null, "Valid Task", "Description", null,
                null, false, false, new User(1L, "test", "pass"));

        when(taskService.getAllTasksByUserId(taskId)).thenReturn(List.of(existingTask));

        ResponseEntity<Void> responseEntity = taskController.deleteTask(taskId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void createTask_InvalidTask_NullTitle_ReturnsBadRequest() {
        User user = new User(1L, "testuser", "password");
        Task taskToCreate = new Task(null, null, "Description",
                null, null, false, false, user);

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
        Task taskToCreate = new Task(null, "  ", "Description", null,
                null, false, false, user);

        try {
            ResponseEntity<Task> responseEntity = taskController.createTask(taskToCreate);
            assertTrue(true);
        } catch (InvalidDataException e) {
            assertEquals("Task title cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void createTask_InvalidTask_NullUser_ReturnsBadRequest() {
        Task taskToCreate = new Task(null, "Valid Task", "Description", null,
                null, false, false, new User(1L, "test", "pass"));

        try {
            ResponseEntity<Task> responseEntity = taskController.createTask(taskToCreate);
            assertTrue(true);
        } catch (InvalidDataException e) {
            assertEquals("User not found for userId: 1", e.getMessage());
        }
    }

    @Test
    public void createTask_InvalidTask_NullUserId_ReturnsBadRequest() {
        Task taskToCreate = new Task(null, "Valid Task", "Description",
                null, null, false, false, null);

        try {
            ResponseEntity<Task> responseEntity = taskController.createTask(taskToCreate);
            assertTrue(true);
        } catch (InvalidDataException e) {
            assertEquals("User not found for userId: null", e.getMessage());
        }
    }
}
