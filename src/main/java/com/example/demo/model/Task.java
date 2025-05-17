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
}
