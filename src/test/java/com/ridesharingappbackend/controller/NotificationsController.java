package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.NotificationsDTO;
import com.ridesharingappbackend.enums.NotificationType;
import com.ridesharingappbackend.service.NotificationsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationsControllerTest {

    @InjectMocks
    private NotificationsController notificationsController;

    @Mock
    private NotificationsService notificationsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private NotificationsDTO createSampleNotification() {
        return new NotificationsDTO(
                1,
                1,
                "New ride assigned",
                NotificationType.RIDE_UPDATE,
                false,
                LocalDateTime.now(),
                1
        );
    }

    @Test
    void getAllNotifications_success() {
        NotificationsDTO notification = createSampleNotification();
        when(notificationsService.getAllNotifications(1)).thenReturn(List.of(notification));

        List<NotificationsDTO> result = notificationsController.getAllNotifications(1);

        assertEquals(1, result.size());
        assertEquals(notification, result.get(0));
    }

    @Test
    void getNotificationById_found() {
        NotificationsDTO notification = createSampleNotification();
        when(notificationsService.getNotificationById(1, 1)).thenReturn(Optional.of(notification));

        ResponseEntity<NotificationsDTO> response = notificationsController.getNotificationById(1, 1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(notification, response.getBody());
    }

    @Test
    void getNotificationById_notFound() {
        when(notificationsService.getNotificationById(999, 1)).thenReturn(Optional.empty());

        ResponseEntity<NotificationsDTO> response = notificationsController.getNotificationById(999, 1);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void createNotification_success() {
        NotificationsDTO input = createSampleNotification();
        when(notificationsService.createNotification(input, 1)).thenReturn(input);

        NotificationsDTO result = notificationsController.createNotification(input, 1);

        assertEquals(input, result);
    }

    @Test
    void deleteNotification_success() {
        doNothing().when(notificationsService).deleteNotification(1, 1);

        ResponseEntity<Void> response = notificationsController.deleteNotification(1, 1);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void markAsRead_success() {
        NotificationsDTO updated = createSampleNotification();
        updated.setIsRead(true);

        when(notificationsService.markAsRead(1, 1)).thenReturn(updated);

        ResponseEntity<NotificationsDTO> response = notificationsController.markAsRead(1, 1);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().getIsRead());
    }

    @Test
    void markAsRead_notFound() {
        when(notificationsService.markAsRead(999, 1)).thenReturn(null);

        ResponseEntity<NotificationsDTO> response = notificationsController.markAsRead(999, 1);

        assertEquals(404, response.getStatusCodeValue());
    }
}
