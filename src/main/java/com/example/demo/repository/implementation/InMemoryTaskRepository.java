package com.example.demo.repository.implementation;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class InMemoryTaskRepository implements TaskRepository {
    private final List<Task> tasks = new ArrayList<>();
    private static final AtomicLong taskId = new AtomicLong(1);

    @Override
    public List<Task> getPendingTasks() {
        return tasks.stream().filter(t -> !t.isCompleted() && !t.isDeleted()).collect(Collectors.toList());
    }

    @Override
    public List<Task> getAllTasksByUserId(Long userId) {
        return tasks.stream()
                .filter(t -> !t.isDeleted() && t.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Task createTask(Task task) {
        task.setId(taskId.getAndIncrement());
        tasks.add(task);
        return task;
    }

    @Override
    public void deleteTask(Long id) {
        tasks.stream().filter(t -> t.getId().equals(id)).forEach(t -> t.setDeleted(true));
    }

    @Override
    public Task getTaskById(Long id) {
        return tasks.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }
}
