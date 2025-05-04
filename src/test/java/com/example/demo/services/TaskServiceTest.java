package com.example.demo.services;

import com.example.demo.db.Task;
import com.example.demo.db.User;
import com.example.demo.db.Notification;
import com.example.demo.db.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @InjectMocks
    private TaskService taskService;

    private User testUser;
    private Task testTask;
    private Notification testNotification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User(1L, "testuser", "password");
        testTask = new Task(1L,"Test Task", "Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), false, false, testUser);
        testNotification = new Notification();
        testNotification.setId(1L);
    }

    @Test
    void createTask_Success() {
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task createdTask = taskService.createTask(testTask);

        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
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
        when(taskRepository.findByCompletedAndDeleted(false, false)).thenReturn(Arrays.asList(testTask));

        List<Task> tasks = taskService.getPendingTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findByCompletedAndDeleted(false, false);
    }

    @Test
    void getPendingTasks_NoTasksFound() {
        when(taskRepository.findByCompletedAndDeleted(false, false)).thenReturn(Collections.emptyList());

        List<Task> tasks = taskService.getPendingTasks();

        assertTrue(tasks.isEmpty());
        verify(taskRepository, times(1)).findByCompletedAndDeleted(false, false);
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
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        taskService.deleteTask(999L);

        verify(taskRepository, times(1)).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }
}
