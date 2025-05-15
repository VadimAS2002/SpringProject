package com.example.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class Notification {

    private Long id;
    private String message;
    private LocalDateTime timestamp;
    private boolean read;
    private User user;

    public Notification(Long id, String message, User user, boolean read, LocalDateTime timestamp) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.read = read;
        this.timestamp = timestamp;
    }
}
