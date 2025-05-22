package com.example.demo.service;

import com.example.demo.exception.InvalidDataException;
import com.example.demo.model.Notification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final List<Notification> notifications = new ArrayList<>();
    private static final AtomicLong notificationId = new AtomicLong(1);

    public NotificationService() {

    }

    public List<Notification> getAllNotificationsForUser(Long userId) {
        return notifications.stream()
                .filter(n -> n.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Notification> getPendingNotifications() {
        return notifications.stream().filter(n -> !n.isRead()).collect(Collectors.toList());
    }

    public void createNotification(Long userId, String message) {
        if (message == null || message.isEmpty()) {
            throw new InvalidDataException("Notification message cannot be empty.");
        }
        Notification notification = new Notification(null, message, userId, false, LocalDateTime.now());
        notification.setId(notificationId.getAndIncrement());
        notifications.add(notification);
    }
}
