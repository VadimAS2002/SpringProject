package com.example.demo.repository.implementation;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class InMemoryNotificationRepository implements NotificationRepository {
    private final List<Notification> notifications = new ArrayList<>();
    private static final AtomicLong notificationId = new AtomicLong(1);

    @Override
    public List<Notification> getAllNotificationsForUser(User user) {
        return notifications.stream()
                .filter(n -> n.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> getPendingNotifications() {
        return notifications.stream().filter(n -> !n.isRead()).collect(Collectors.toList());
    }

    @Override
    public Notification createNotification(Notification notification) {
        notification.setId(notificationId.getAndIncrement());
        notifications.add(notification);
        return notification;
    }
}
