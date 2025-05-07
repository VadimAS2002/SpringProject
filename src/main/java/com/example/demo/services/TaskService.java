package com.example.demo.services;

import com.example.demo.models.Task;
import com.example.demo.models.User;
import com.example.demo.models.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final Map<Long, Task> tasks = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    public Task createTask(Task task) {
        User user = userService.getUserById(task.getUser().getId());
        if (user == null) {
            throw new IllegalArgumentException("User not found for userId: " + task.getUser().getId());
        }

        task.setId(idGenerator.getAndIncrement());
        task.setCreationDate(LocalDateTime.now());
        tasks.put(task.getId(), task);

        Notification notification = new Notification();
        notification.setMessage("Task '" + task.getTitle() + "' created.");
        notification.setUser(user);
        notification.setTask(task);
        notificationService.createNotification(notification);

        return task;
    }

    public Task getTaskById(Long id) {
        return tasks.get(id);
    }

    public List<Task> getTasksByUserId(Long userId) {
        return tasks.values().stream()
                .filter(task -> !task.getDeleted() && task.getUser() != null && task.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Task> getPendingTasks() {
        return tasks.values().stream()
                .filter(task -> !task.getCompleted() && !task.getDeleted() && task.getUser() != null)
                .collect(Collectors.toList());
    }

    public void deleteTask(Long id) {
        Task task = tasks.get(id);
        if (task != null) {
            task.setDeleted(true);
        }
    }
}
