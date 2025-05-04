package com.example.demo.services;

import com.example.demo.db.Task;
import com.example.demo.db.User;
import com.example.demo.db.Notification;
import com.example.demo.db.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TaskService taskService;

    private User testUser;
    private Task testTask;
    private Notification testNotification;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setCreationDate(LocalDateTime.now());
        testTask.setTargetDate(LocalDateTime.now().plusDays(7));
        testTask.setCompleted(false);
        testTask.setDeleted(false);
        testTask.setUser(testUser);

        testNotification = new Notification();
        testNotification.setId(1L);
    }

    @Test
    void createTask_Success() {
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        when(notificationService.createNotification(any(Notification.class))).thenReturn(testNotification);

        Task createdTask = taskService.createTask(testTask);

        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(notificationService, times(1)).createNotification(any(Notification.class));
    }

    @Test
    void createTask_UserNotFound() {
        when(userService.getUserById(1L)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(testTask);
        });

        assertEquals("User not found for userId: 1", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
        verify(notificationService, never()).createNotification(any(Notification.class));
    }

    @Test
    void getTasksByUserId_Success() {
        when(taskRepository.findByUserId(1L)).thenReturn(Arrays.asList(testTask));

        List<Task> tasks = taskService.getTasksByUserId(1L);

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getTasksByUserId_NoTasksFound() {
        when(taskRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        List<Task> tasks = taskService.getTasksByUserId(1L);

        assertTrue(tasks.isEmpty());
        verify(taskRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getPendingTasks_Success() {
        when(taskRepository.findByCompleted(false)).thenReturn(Arrays.asList(testTask));

        List<Task> tasks = taskService.getPendingTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findByCompleted(false);
    }

    @Test
    void getPendingTasks_NoTasksFound() {
        when(taskRepository.findByCompleted(false)).thenReturn(Collections.emptyList());

        // Act
        List<Task> tasks = taskService.getPendingTasks();

        // Assert
        assertTrue(tasks.isEmpty());
        verify(taskRepository, times(1)).findByCompleted(false);
    }

    @Test
    void deleteTask_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        taskService.deleteTask(1L);

        assertTrue(testTask.getDeleted());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void deleteTask_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }
}
