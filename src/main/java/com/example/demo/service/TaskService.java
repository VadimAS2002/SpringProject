package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final NotificationService notificationService;
    private UserService userService;

    public TaskService(TaskRepository taskRepository, NotificationService notificationService,
                       UserService userService) {
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    public List<Task> getPendingTasks() {
        return taskRepository.getPendingTasks();
    }

    public List<Task> getAllTasksByUserId(Long userId) {
        return taskRepository.getAllTasksByUserId(userId);
    }

    public Task createTask(Task task) {
        User user = userService.getUserById(task.getUser().getId());
        if (user == null) {
            throw new IllegalArgumentException("User not found for userId: " + task.getUser().getId());
        }

        Task createdTask = taskRepository.createTask(task);
        task.setUser(user);
        notificationService.createNotification(task.getUser(), "Task " + task.getTitle() + " created!");
        return createdTask;
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.getTaskById(id);
        if (task != null) {
            notificationService.createNotification(task.getUser(), "Task " + task.getTitle() + " deleted!");
            taskRepository.deleteTask(id);
        }
    }
}
