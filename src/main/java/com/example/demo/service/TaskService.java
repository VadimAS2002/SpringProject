package com.example.demo.service;

import com.example.demo.exception.InvalidDataException;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final List<Task> tasks = new ArrayList<>();
    private static final AtomicLong taskId = new AtomicLong(1);
    private final NotificationService notificationService;
    private UserService userService;

    public TaskService(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    public List<Task> getPendingTasks() {
        return tasks.stream().filter(t -> !t.isCompleted() && !t.isDeleted()).collect(Collectors.toList());
    }

    public List<Task> getAllTasksByUserId(Long userId) {
        return tasks.stream()
                .filter(t -> !t.isDeleted() && t.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Task createTask(Task task) {
        User user = userService.getUserById(task.getUserId());
        if (user == null)
            throw new IllegalArgumentException("User not found for userId: " + task.getUserId());
        if (task.getDescription() == null || task.getDescription().isEmpty())
            throw new InvalidDataException("Task description cannot be empty.");
        if (task.getTitle() == null || task.getTitle().isEmpty())
            throw new InvalidDataException("Task title cannot be empty.");

        task.setId(taskId.getAndIncrement());
        task.setUserId(user.getId());
        tasks.add(task);
        notificationService.createNotification(task.getUserId(), "Task " + task.getTitle() + " created!");
        return task;
    }

    public void deleteTask(Long id) {
        Task task = tasks.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
        if (task != null) {
            notificationService.createNotification(task.getUserId(), "Task " + task.getTitle() + " deleted!");
            tasks.stream().filter(t -> t.getId().equals(id)).forEach(t -> t.setDeleted(true));
        }
    }
}
