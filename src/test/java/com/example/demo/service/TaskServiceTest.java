package com.example.demo.service;

import com.example.demo.exception.InvalidDataException;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.InMemoryImpl.InMemoryTaskRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    private User testUser;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        taskRepository = new InMemoryTaskRepository();
        notificationService = new NotificationService(notificationRepository);
        userService = new UserService(notificationService, userRepository);
        taskService = new TaskService(taskRepository, notificationService, userService);

        testUser = userService.registerUser(new User(null, "testuser", "password"));
    }

    @Test
    void getPendingTasks_ReturnsPendingTasks() {
        Task task1 = taskRepository.save(new Task(null, "Test 1", "Description 1", LocalDateTime.now(),
                null, false, false, testUser));
        Task task2 = taskRepository.save(new Task(null, "Test 2", "Description 2", LocalDateTime.now(),
                null, true, false, testUser));

        List<Task> actualTasks = taskService.getPendingTasks();

        assertEquals(1, actualTasks.size());
    }

    @Test
    void getAllTasksByUserId_ReturnsTasksForUser() {
        Task task1 = new Task(null, "Test 1", "Description 1", LocalDateTime.now(),
                null, false, false, testUser);
        Task task2 = new Task(null, "Test 2", "Description 2", LocalDateTime.now(),
                null, false, false, testUser);
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
                null, false, false, testUser);

        Task createdTask = taskService.createTask(task);

        assertNotNull(createdTask.getId());
        assertEquals("Test Task", createdTask.getTitle());
        assertEquals("Test Description", createdTask.getDescription());
        assertEquals(testUser, createdTask.getUser());
    }

    @Test
    void deleteTask_SuccessfulDeletion() {
        Task task = new Task(null, "Test Task", "Test Description", LocalDateTime.now(),
                null, false, false, testUser);
        Task createdTask = taskService.createTask(task);
        Long taskId = createdTask.getId();

        taskService.deleteTask(taskId);

        List<Task> remainingTasks = taskService.getAllTasksByUserId(testUser.getId());
        assertEquals(0, remainingTasks.size());
    }
}
