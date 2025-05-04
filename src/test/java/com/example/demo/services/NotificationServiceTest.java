package com.example.demo.services;

import com.example.demo.db.Notification;
import com.example.demo.db.User;
import com.example.demo.db.Task;
import com.example.demo.db.repositories.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private User testUser;
    private Task testTask;
    private Notification testNotification;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");

        testNotification = new Notification();
        testNotification.setId(1L);
        testNotification.setMessage("Test Notification");
        testNotification.setUser(testUser);
        testNotification.setTask(testTask);
    }

    @Test
    void createNotification_Success() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        Notification createdNotification = notificationService.createNotification(new Notification());

        assertNotNull(createdNotification);
        assertEquals("Test Notification", createdNotification.getMessage());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void getNotificationsByUserId_Success() {
        when(notificationRepository.findByUserId(1L)).thenReturn(Arrays.asList(testNotification));

        List<Notification> notifications = notificationService.getNotificationsByUserId(1L);

        assertNotNull(notifications);
        assertEquals(1, notifications.size());
        assertEquals("Test Notification", notifications.get(0).getMessage());
        verify(notificationRepository, times(1)).findByUserId(1L);
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
