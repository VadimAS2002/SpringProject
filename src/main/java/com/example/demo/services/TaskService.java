package com.example.demo.services;

import com.example.demo.db.Task;
import com.example.demo.db.User;
import com.example.demo.db.Notification;
import com.example.demo.db.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public Task createTask(Task task) {
        User user = userService.getUserById(task.getUser().getId());
        if (user == null) {
            throw new IllegalArgumentException("User not found for userId: " + task.getUser().getId());
        }

        task.setCreationDate(LocalDateTime.now());
        Task savedTask = taskRepository.save(task);

        Notification notification = new Notification();
        notification.setMessage("Task '" + task.getTitle() + "' created.");
        notification.setUser(user);
        notification.setTask(savedTask);
        notificationService.createNotification(notification);

        return savedTask;
    }

    public Task getTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElse(null);
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public List<Task> getPendingTasks() {
        return taskRepository.findByCompletedAndDeleted(false, false);
    }

    public void deleteTask(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setDeleted(true);
            taskRepository.save(task);
        }
    }
}
