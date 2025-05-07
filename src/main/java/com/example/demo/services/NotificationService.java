package com.example.demo.services;

import com.example.demo.models.Notification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final Map<Long, Notification> notifications = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Notification createNotification(Notification notification) {
        notification.setId(idGenerator.getAndIncrement());
        notification.setTimestamp(LocalDateTime.now());
        notifications.put(notification.getId(), notification);
        return notification;
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        return notifications.values().stream()
                .filter(notification -> notification.getUser() != null && notification.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Notification> getPendingNotifications(Long userId) {
        return notifications.values().stream()
                .filter(notification -> !notification.getRead() && notification.getUser() != null && notification.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }
}
