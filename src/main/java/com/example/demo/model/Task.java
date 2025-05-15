package com.example.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class Task {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private LocalDateTime targetDate;
    private boolean completed;
    private boolean deleted;
    private User user;

    public Task(Long id, String title, String description, LocalDateTime creationDate,
                LocalDateTime targetDate, boolean completed, boolean deleted, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.targetDate = targetDate;
        this.completed = completed;
        this.deleted = deleted;
        this.user = user;
    }
}
