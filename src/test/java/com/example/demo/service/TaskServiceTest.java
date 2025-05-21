package com.example.demo.service;

import com.example.demo.exception.InvalidDataException;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    private User testUser;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(notificationService, userService);

        testUser = new User(null, "testuser", "password");
        testUser = userService.registerUser(testUser);

        List<Task> existingTasks = taskService.getAllTasksByUserId(testUser.getId());
        existingTasks.forEach(task -> taskService.deleteTask(task.getId()));
    }

    @Test
    void getPendingTasks_ReturnsPendingTasks() {
        Task task1 = new Task(null, "Test 1", "Description 1", LocalDateTime.now(),
                null, false, false, testUser.getId());
        Task task2 = new Task(null, "Test 2", "Description 2", LocalDateTime.now(),
                null, true, false, testUser.getId());
        taskService.createTask(task1);
        taskService.createTask(task2);

        List<Task> pendingTasks = taskService.getPendingTasks();

        assertEquals(1, pendingTasks.size());
        assertEquals("Test 1", pendingTasks.get(0).getTitle());
    }

    @Test
    void getAllTasksByUserId_ReturnsTasksForUser() {
        Task task1 = new Task(null, "Test 1", "Description 1", LocalDateTime.now(),
                null, false, false, testUser.getId());
        Task task2 = new Task(null, "Test 2", "Description 2", LocalDateTime.now(),
                null, false, false, testUser.getId());
        taskService.createTask(task1);
        taskService.createTask(task2);

        List<Task> userTasks = taskService.getAllTasksByUserId(testUser.getId());

        assertTrue(userTasks.size() >= 2);
        assertEquals("Test 1", userTasks.get(0).getTitle());
        assertEquals("Test 2", userTasks.get(1).getTitle());
    }

    @Test
    void createTask_SuccessfulCreation() {
        Task task = new Task(null, "Test Task", "Test Description", LocalDateTime.now(),
                null, false, false, testUser.getId());

        Task createdTask = taskService.createTask(task);

        assertNotNull(createdTask.getId());
        assertEquals("Test Task", createdTask.getTitle());
        assertEquals("Test Description", createdTask.getDescription());
        assertEquals(testUser.getId(), createdTask.getUserId());
    }

    @Test
    void createTask_InvalidDescription_ThrowsException() {
        Task task = new Task(null, "Test Task", "", LocalDateTime.now(),
                null, false, false, testUser.getId());

        assertThrows(InvalidDataException.class, () -> {
            taskService.createTask(task);
        });
    }

    @Test
    void createTask_InvalidTitle_ThrowsException() {
        Task task = new Task(null, "", "Test Description", LocalDateTime.now(),
                null, false, false, testUser.getId());

        assertThrows(InvalidDataException.class, () -> {
            taskService.createTask(task);
        });
    }

    @Test
    void deleteTask_SuccessfulDeletion() {
        Task task = new Task(null, "Test Task", "Test Description", LocalDateTime.now(),
                null, false, false, testUser.getId());
        Task createdTask = taskService.createTask(task);
        Long taskId = createdTask.getId();

        taskService.deleteTask(taskId);

        List<Task> remainingTasks = taskService.getAllTasksByUserId(testUser.getId());
        assertEquals(0, remainingTasks.size());
    }
}
