package com.example.demo.db.repositories;

import com.example.demo.db.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> findByCompletedAndDeleted(Boolean completed, Boolean deleted);
}
