package com.example.demo.services;

import com.example.demo.models.Task;
import com.example.demo.models.User;
import com.example.demo.models.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_Success() {
        User user = new User(1L, "testuser", "password");
        Task task = new Task(null, "Test Task", "Description", LocalDateTime.now(),
                LocalDateTime.now().plusDays(1), false, false, user);

        when(userService.getUserById(1L)).thenReturn(user);
        when(notificationService.createNotification(any(Notification.class))).thenAnswer(i -> i.getArgument(0));

        Task createdTask = taskService.createTask(task);

        assertNotNull(createdTask.getId());
        assertEquals("Test Task", createdTask.getTitle());
        assertEquals(user, createdTask.getUser());
        verify(userService, times(1)).getUserById(1L);
        verify(notificationService, times(1)).createNotification(any(Notification.class));
    }

    @Test
    void createTask_UserNotFound() {
        Task task = new Task(null, "Test Task", "Description", LocalDateTime.now(),
                LocalDateTime.now().plusDays(1), false, false, new User(1L, "testuser", "password"));

        when(userService.getUserById(1L)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(task);
        });

        assertEquals("User not found for userId: 1", exception.getMessage());
        verify(userService, times(1)).getUserById(1L);
        verify(notificationService, never()).createNotification(any(Notification.class));
    }

    @Test
    void getTasksByUserId_Success() {
        User user1 = new User(1L, "user1", "password");
        Task task1 = new Task(1L, "Task 1", "Desc", LocalDateTime.now(), LocalDateTime.now(),
                false, false, user1);
        Task task2 = new Task(2L, "Task 2", "Desc", LocalDateTime.now(), LocalDateTime.now(),
                false, false, user1);
        taskService.createTask(task1);
        taskService.createTask(task2);
        when(userService.getUserById(1L)).thenReturn(user1);

        List<Task> tasks = taskService.getTasksByUserId(1L);

        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().allMatch(task -> task.getUser().getId().equals(1L)));
    }

    @Test
    void getTasksByUserId_NoTasksFound() {
        when(userService.getUserById(1L)).thenReturn(new User(1L, "user1", "password"));

        List<Task> tasks = taskService.getTasksByUserId(1L);

        assertEquals(0, tasks.size());
    }

    @Test
    void getPendingTasks_Success() {
        User user1 = new User(1L, "user1", "password");
        Task task1 = new Task(1L, "Task 1", "Desc", LocalDateTime.now(), LocalDateTime.now(),
                false, false, user1);
        taskService.createTask(task1);
        when(userService.getUserById(1L)).thenReturn(user1);

        List<Task> tasks = taskService.getPendingTasks();

        assertEquals(1, tasks.size());
    }
}
