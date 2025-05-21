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
    private Long userId;

    public Notification(Long id, String message, Long userId, boolean read, LocalDateTime timestamp) {
        this.id = id;
        this.message = message;
        this.userId = userId;
        this.read = read;
        this.timestamp = timestamp;
    }
}
