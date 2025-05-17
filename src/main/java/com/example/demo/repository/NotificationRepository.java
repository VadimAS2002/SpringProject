package com.example.demo.repository;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository {
    List<Notification> getAllNotificationsForUser(User user);
    List<Notification> getPendingNotifications();
    Notification save(Notification notification);
}
