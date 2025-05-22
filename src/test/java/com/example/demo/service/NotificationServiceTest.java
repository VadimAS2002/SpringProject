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

    @Test
    void createNotification_ValidInput_CreatesNotification() {
        notificationService.createNotification(1L, "Test notification");
        List<Notification> notifications = notificationService.getAllNotificationsForUser(1L);
        assertEquals(1, notifications.size());
        assertEquals("Test notification", notifications.get(0).getMessage());
    }

    @Test
    void createNotification_EmptyMessage_ThrowsInvalidDataException() {
        assertThrows(InvalidDataException.class, () -> notificationService.createNotification(1L, ""));
    }

    @Test
    void getAllNotificationsForUser_ExistingNotifications_ReturnsNotifications() {
        notificationService.createNotification(1L, "Test notification 1");
        notificationService.createNotification(1L, "Test notification 2");
        notificationService.createNotification(2L, "Test notification 3");

        List<Notification> notifications = notificationService.getAllNotificationsForUser(1L);

        assertEquals(2, notifications.size());
        assertEquals("Test notification 1", notifications.get(0).getMessage());
        assertEquals("Test notification 2", notifications.get(1).getMessage());
    }

    @Test
    void getAllNotificationsForUser_NoNotifications_ReturnsEmptyList() {
        List<Notification> notifications = notificationService.getAllNotificationsForUser(1L);
        assertTrue(notifications.isEmpty());
    }

    @Test
    void getPendingNotifications_ExistingPendingNotifications_ReturnsPendingNotifications() {
        notificationService.createNotification(1L, "Test notification 1");
        notificationService.createNotification(1L, "Test notification 2");
        List<Notification> pendingNotifications = notificationService.getPendingNotifications();
        assertEquals(2, pendingNotifications.size());
    }

    @Test
    void getPendingNotifications_NoPendingNotifications_ReturnsEmptyList() {
        List<Notification> pendingNotifications = notificationService.getPendingNotifications();
        assertTrue(pendingNotifications.isEmpty());
    }
}
