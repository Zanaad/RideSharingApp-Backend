package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.NotificationsDTO;
import com.ridesharingappbackend.mappers.NotificationsMapper;
import com.ridesharingappbackend.models.Notifications;
import com.ridesharingappbackend.repository.NotificationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationsServiceTest {

    @Mock
    private NotificationsRepository notificationRepository;

    @Mock
    private NotificationsMapper mapper;

    @InjectMocks
    private NotificationsService notificationsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllNotifications_ReturnsDTOList() {
        Notifications notif = new Notifications(); notif.setId(1);
        NotificationsDTO dto = new NotificationsDTO(); dto.setId(1);

        when(notificationRepository.findAll()).thenReturn(List.of(notif));
        when(mapper.toDTO(notif)).thenReturn(dto);

        List<NotificationsDTO> result = notificationsService.getAllNotifications();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getNotificationById_WhenExists_ReturnsDTO() {
        Notifications notif = new Notifications(); notif.setId(1);
        NotificationsDTO dto = new NotificationsDTO(); dto.setId(1);

        when(notificationRepository.findById(1)).thenReturn(Optional.of(notif));
        when(mapper.toDTO(notif)).thenReturn(dto);

        Optional<NotificationsDTO> result = notificationsService.getNotificationById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getNotificationById_WhenNotExists_ReturnsEmptyOptional() {
        when(notificationRepository.findById(99)).thenReturn(Optional.empty());

        Optional<NotificationsDTO> result = notificationsService.getNotificationById(99);

        assertFalse(result.isPresent());
    }

    @Test
    void getNotificationsByUserId_ReturnsDTOList() {
        Notifications notif = new Notifications(); notif.setId(1);
        NotificationsDTO dto = new NotificationsDTO(); dto.setId(1);

        when(notificationRepository.findByUserId(7)).thenReturn(List.of(notif));
        when(mapper.toDTO(notif)).thenReturn(dto);

        List<NotificationsDTO> result = notificationsService.getNotificationsByUserId(7);

        assertEquals(1, result.size());
    }

    @Test
    void createNotification_SavesAndReturnsDTO() {
        NotificationsDTO dto = new NotificationsDTO(); dto.setId(1);
        Notifications notif = new Notifications(); notif.setId(1);

        when(mapper.toEntity(dto)).thenReturn(notif);
        when(notificationRepository.save(notif)).thenReturn(notif);
        when(mapper.toDTO(notif)).thenReturn(dto);

        NotificationsDTO result = notificationsService.createNotification(dto);

        assertEquals(1, result.getId());
    }

    @Test
    void deleteNotification_DeletesById() {
        doNothing().when(notificationRepository).deleteById(1);

        notificationsService.deleteNotification(1);

        verify(notificationRepository).deleteById(1);
    }

    @Test
    void markAsRead_WhenExists_SetsIsReadAndReturnsDTO() {
        Notifications notif = new Notifications(); notif.setId(1); notif.setIsRead(false);
        NotificationsDTO dto = new NotificationsDTO(); dto.setId(1); dto.setIsRead(true);

        when(notificationRepository.findById(1)).thenReturn(Optional.of(notif));
        when(notificationRepository.save(notif)).thenReturn(notif);
        when(mapper.toDTO(notif)).thenReturn(dto);

        NotificationsDTO result = notificationsService.markAsRead(1);

        assertTrue(notif.getIsRead()); // verify isRead was set
        assertEquals(1, result.getId());
    }

    @Test
    void markAsRead_WhenNotExists_ReturnsNull() {
        when(notificationRepository.findById(42)).thenReturn(Optional.empty());

        NotificationsDTO result = notificationsService.markAsRead(42);

        assertNull(result);
    }
}
