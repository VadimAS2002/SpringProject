package com.example.demo.repository;

import com.example.demo.model.Notification;
import com.example.demo.model.User;

import java.util.List;

public interface NotificationRepository {
    List<Notification> getAllNotificationsForUser(User user);
    List<Notification> getPendingNotifications();
    Notification createNotification(Notification notification);
}
