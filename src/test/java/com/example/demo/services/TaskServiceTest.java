package com.example.demo.services;

import com.example.demo.models.Task;
import com.example.demo.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    void createTask_Success() {
        User user = new User(1L, "testuser", "password");
        Task task = new Task(null, "Test Task", "Description", null,
                LocalDateTime.now().plusDays(1), false, false, user);

        Task createdTask = taskService.createTask(task);

        assertNotNull(createdTask.getId());
        assertEquals("Test Task", createdTask.getTitle());
    }

    @Test
    void getTasksByUserId_Success() {
        User user1 = new User(1L, "user1", "password");
        User user2 = new User(2L, "user2", "password");
        Task task1 = new Task(null, "Task 1", "Desc", null, LocalDateTime.now(),
                false, false, user1);
        Task task2 = new Task(null, "Task 2", "Desc", null, LocalDateTime.now(),
                false, false, user1);

        taskService.createTask(task1);
        taskService.createTask(task2);

        List<Task> tasks = taskService.getTasksByUserId(1L);

        assertEquals(1, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
    }

    @Test
    void getTasksByUserId_NoTasksFound() {
        User user1 = new User(1L, "user1", "password");
        User user2 = new User(2L, "user2", "password");
        Task task2 = new Task(null, "Task 2", "Desc", null, LocalDateTime.now(),
                false, false,  user2);

        taskService.createTask(task2);

        List<Task> tasks = taskService.getTasksByUserId(1L);

        assertEquals(0, tasks.size());
    }

    @Test
    void getPendingTasks_Success() {
        User user1 = new User(1L, "user1", "password");
        Task task1 = new Task(null, "Task 1", "Desc", null, LocalDateTime.now(),
                false, false,  user1);
        Task task2 = new Task(null, "Task 2", "Desc", null, LocalDateTime.now(),
                false, false,  user1);
        task1.setCompleted(true);

        taskService.createTask(task1);
        taskService.createTask(task2);

        List<Task> pendingTasks = taskService.getPendingTasks();

        assertEquals(1, pendingTasks.size());
        assertEquals("Task 2", pendingTasks.get(0).getTitle());
    }

    @Test
    void getPendingTasks_NoTasksFound() {
        User user1 = new User(1L, "user1", "password");
        Task task1 = new Task(null, "Task 1", "Desc", null, LocalDateTime.now(),
                false, false,  user1);
        task1.setCompleted(true);

        taskService.createTask(task1);

        List<Task> pendingTasks = taskService.getPendingTasks();

        assertEquals(0, pendingTasks.size());
    }

    @Test
    void deleteTask_Success() {
        User user = new User(1L, "testuser", "password");
        Task task = new Task(null, "Test Task", "Description", null,
                LocalDateTime.now().plusDays(1), false, false, user);
        Task createdTask = taskService.createTask(task);
        Long taskId = createdTask.getId();

        taskService.deleteTask(taskId);

        Task deletedTask = taskService.getTaskById(taskId);
        assertNotNull(deletedTask);
        assertTrue(deletedTask.getDeleted());
    }
}
