package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime creationDate;
    private LocalDateTime targetDate;

    @Column(nullable = false)
    private boolean completed;

    @Column(nullable = false)
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Task(Long id, String title, String description, LocalDateTime creationDate, LocalDateTime targetDate,
                boolean completed, boolean deleted, User user) {
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
