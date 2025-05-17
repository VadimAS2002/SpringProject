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
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean read;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Notification(Long id, String message, User user, boolean read, LocalDateTime timestamp) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.read = read;
        this.timestamp = timestamp;
    }
}
