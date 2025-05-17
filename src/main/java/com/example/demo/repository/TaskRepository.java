package com.example.demo.repository;

import com.example.demo.model.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository {
    List<Task> getPendingTasks();
    List<Task> getAllTasksByUserId(Long userId);
    Task save(Task task);
    void deleteTask(Long id);
    Task getTaskById(Long id);
}
