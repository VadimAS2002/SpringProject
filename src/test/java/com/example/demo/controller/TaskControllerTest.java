package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.NotificationService;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskControllerTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @InjectMocks
    private TaskController taskController;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private final LocalDateTime now = LocalDateTime.now();

    private User testUser;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(notificationRepository);
        userService = new UserService(notificationService, userRepository);
        taskService = new TaskService(taskRepository, notificationService, userService);
        taskController = new TaskController(taskService);

        testUser = userRepository.save(new User(null, "testuser", "password"));
    }

    @Test
    void getPendingTasks_ReturnsPendingTasks() {
        ResponseEntity<List<Task>> responseEntity = new TaskController(taskService).getPendingTasks();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getAllTasksByUserId_ReturnsTasksForUser() {
        Task task1 = taskService.createTask(new Task(1L, "Task 1", "Description",
                now, now.plusDays(1), false, false, testUser));
        Task task2 = taskService.createTask(new Task(null, "Task 2", "Description 2", now,
                now.plusDays(1), false, false, testUser));

        ResponseEntity<List<Task>> responseEntity = new TaskController(taskService).getAllTasksByUserId(testUser.getId());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().size());
    }

    @Test
    void createTask_SuccessfulCreation() {
        Task task = new Task(null, "Test Task", "Test Description", now, now.plusDays(1),
                false, false, testUser);

        ResponseEntity<Task> responseEntity = new TaskController(taskService).createTask(task);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody().getId());
        assertEquals("Test Task", responseEntity.getBody().getTitle());
    }

    @Test
    void deleteTask_SuccessfulDeletion() {
        Task task = taskService.createTask(new Task(null, "Test Task", "Test Description",
                now, now.plusDays(1), false, false, testUser));

        ResponseEntity<Void> responseEntity = new TaskController(taskService).deleteTask(task.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        assertNull(taskService.getAllTasksByUserId(testUser.getId()).stream()
                .filter(t -> t.getId().equals(task.getId()))
                .findFirst()
                .orElse(null));
    }
}
