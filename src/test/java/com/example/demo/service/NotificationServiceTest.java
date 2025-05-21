package com.example.demo.service;

import com.example.demo.exception.InvalidDataException;
import com.example.demo.model.Notification;
import com.example.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService();
    }

    User user = new User(1L, "testuser", "password");

    @Test
    public void createNotification_SuccessfulCreation() {
        notificationService.createNotification(user.getId(), "This is a test notification.");
        List<Notification> actualNotifications = notificationService.getAllNotificationsForUser(user.getId());
        assertEquals(1, actualNotifications.size()); // Check size instead of message directly
        assertEquals("This is a test notification.", actualNotifications.get(0).getMessage());
    }

    @Test
    public void createNotification_InvalidMessage_ThrowsException() {
        String message = "";

        assertThrows(InvalidDataException.class, () -> {
            notificationService.createNotification(user.getId(), message);
        });
    }

    @Test
    public void getPendingNotifications_ReturnsNotifications() {
        notificationService.createNotification(user.getId(), "This is a test notification.");
        List<Notification> actualNotifications = notificationService.getPendingNotifications();
        assertEquals(1, actualNotifications.size());
    }
}
