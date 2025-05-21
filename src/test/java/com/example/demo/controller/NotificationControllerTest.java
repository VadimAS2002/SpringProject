package com.example.demo.controller;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationControllerTest {

    @Autowired
    private NotificationController notificationController;

    @Autowired
    private NotificationService notificationService;

    @Test
    void getAllNotificationsForUser_ReturnsNotifications() {
        User user = new User(1L, "testuser", "password");

        notificationService.createNotification(1L, "Notification 1");
        notificationService.createNotification(1L, "Notification 2");

        ResponseEntity<List<Notification>> response = notificationController.getAllNotificationsForUser(user.getId());

        assertEquals(200, response.getStatusCodeValue());
        List<Notification> actualNotifications = response.getBody();
        assertEquals(2, actualNotifications.size());
        assertEquals("Notification 1", actualNotifications.get(0).getMessage());
    }

    @Test
    void getPendingNotificationsForUser_ReturnsPendingNotifications() {
        User user = new User(1L, "testuser", "password");

        ResponseEntity<List<Notification>> response = notificationController.getPendingNotificationsForUser();

        assertEquals(200, response.getStatusCodeValue());
        List<Notification> actualNotifications = response.getBody();
        assertNotNull(actualNotifications);
        if (!actualNotifications.isEmpty()) {
            assertEquals("Pending Notification", actualNotifications.get(0).getMessage());
        }
    }
}
