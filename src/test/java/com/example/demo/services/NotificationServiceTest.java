package com.example.demo.services;

import com.example.demo.models.Notification;
import com.example.demo.models.User;
import com.example.demo.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService();
    }

    @Test
    void createNotification_Success() {
        User user = new User(1L, "testuser", "password");
        Task task = new Task(1L, "Test Task", "Description", LocalDateTime.now(),
                LocalDateTime.now().plusDays(1), false, false, user);
        Notification notification = new Notification();
        notification.setMessage("Test Notification");
        notification.setUser(user);
        notification.setTask(task);

        Notification createdNotification = notificationService.createNotification(notification);

        assertNotNull(createdNotification.getId());
        assertNotNull(createdNotification.getTimestamp());
        assertEquals("Test Notification", createdNotification.getMessage());
        assertEquals(user, createdNotification.getUser());
        assertEquals(task, createdNotification.getTask());
    }

    @Test
    void getNotificationsByUserId_Success() {
        User user1 = new User(1L, "user1", "password");
        User user2 = new User(2L, "user2", "password");
        Task task1 = new Task(1L, "Task 1", "Desc", LocalDateTime.now(), LocalDateTime.now(),
                false, false, user1);
        Task task2 = new Task(2L, "Task 2", "Desc", LocalDateTime.now(), LocalDateTime.now(),
                false, false, user2);
        Notification notification1 = new Notification();
        notification1.setMessage("Notification 1");
        notification1.setUser(user1);
        notification1.setTask(task1);

        Notification notification2 = new Notification();
        notification2.setMessage("Notification 2");
        notification2.setUser(user2);
        notification2.setTask(task2);

        notificationService.createNotification(notification1);
        notificationService.createNotification(notification2);

        List<Notification> notifications = notificationService.getNotificationsByUserId(1L);

        assertEquals(1, notifications.size());
        assertEquals("Notification 1", notifications.get(0).getMessage());
    }

    @Test
    void getNotificationsByUserId_NoNotificationsFound() {
        List<Notification> notifications = notificationService.getNotificationsByUserId(1L);
        assertEquals(0, notifications.size());
    }

    @Test
    void getPendingNotifications_Success() {
        User user1 = new User(1L, "user1", "password");
        Task task1 = new Task(1L, "Task 1", "Desc", LocalDateTime.now(), LocalDateTime.now(),
                false, false, user1);
        Notification notification1 = new Notification();
        notification1.setMessage("Notification 1");
        notification1.setUser(user1);
        notification1.setTask(task1);

        Notification createdNotification = notificationService.createNotification(notification1);

        List<Notification> pendingNotifications = notificationService.getPendingNotifications(1L);

        assertEquals(1, pendingNotifications.size());
        assertFalse(pendingNotifications.get(0).getRead());
    }

    @Test
    void getPendingNotifications_NoPendingNotifications() {
        User user1 = new User(1L, "user1", "password");
        Task task1 = new Task(1L, "Task 1", "Desc", LocalDateTime.now(), LocalDateTime.now(),
                false, false, user1);
        Notification notification1 = new Notification();
        notification1.setMessage("Notification 1");
        notification1.setUser(user1);
        notification1.setTask(task1);
        notification1.setRead(true);

        Notification createdNotification = notificationService.createNotification(notification1);

        List<Notification> pendingNotifications = notificationService.getPendingNotifications(1L);

        assertEquals(0, pendingNotifications.size());
    }
}
