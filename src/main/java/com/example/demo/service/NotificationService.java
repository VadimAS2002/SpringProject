package com.example.demo.service;

import com.example.demo.exception.InvalidDataException;
import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAllNotificationsForUser(User user) {
        return notificationRepository.getAllNotificationsForUser(user);
    }

    public List<Notification> getPendingNotifications() {
        return notificationRepository.getPendingNotifications();
    }

    public void createNotification(User user, String message) {
        if (message == null || message.isEmpty()) {
            throw new InvalidDataException("Notification message cannot be empty.");
        }
        Notification notification = new Notification(null, message, user, false, LocalDateTime.now());
        notificationRepository.createNotification(notification);
    }
}
