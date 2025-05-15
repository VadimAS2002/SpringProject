package com.example.demo.repository;

import com.example.demo.model.Task;

import java.util.List;

public interface TaskRepository {
    List<Task> getPendingTasks();
    List<Task> getAllTasksByUserId(Long userId);
    Task createTask(Task task);
    void deleteTask(Long id);
    Task getTaskById(Long id);
}
