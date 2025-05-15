package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.NotificationsDTO;
import com.ridesharingappbackend.mappers.NotificationsMapper;
import com.ridesharingappbackend.models.Notifications;
import com.ridesharingappbackend.repository.NotificationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationsServiceTest {

    @Mock
    private NotificationsRepository notificationRepository;

    @Mock
    private NotificationsMapper mapper;

    @InjectMocks
    private NotificationsService notificationsService;

    private final Integer companyId = 10;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllNotifications_ReturnsDTOList() {
        Notifications notif = new Notifications();
        notif.setId(1);
        NotificationsDTO dto = new NotificationsDTO();
        dto.setId(1);

        when(notificationRepository.findByCompanyId(companyId)).thenReturn(List.of(notif));
        when(mapper.toDTO(notif)).thenReturn(dto);

        List<NotificationsDTO> result = notificationsService.getAllNotifications(companyId);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getNotificationById_WhenExists_ReturnsDTO() {
        Notifications notif = new Notifications();
        notif.setId(1);
        NotificationsDTO dto = new NotificationsDTO();
        dto.setId(1);

        when(notificationRepository.findByIdAndCompanyId(1, companyId)).thenReturn(Optional.of(notif));
        when(mapper.toDTO(notif)).thenReturn(dto);

        Optional<NotificationsDTO> result = notificationsService.getNotificationById(1, companyId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getNotificationById_WhenNotExists_ReturnsEmptyOptional() {
        when(notificationRepository.findByIdAndCompanyId(99, companyId)).thenReturn(Optional.empty());

        Optional<NotificationsDTO> result = notificationsService.getNotificationById(99, companyId);

        assertFalse(result.isPresent());
    }

    @Test
    void createNotification_SavesAndReturnsDTO() {
        NotificationsDTO dto = new NotificationsDTO();
        dto.setId(1);
        Notifications notif = new Notifications();
        notif.setId(1);

        when(mapper.toEntity(dto)).thenReturn(notif);
        when(notificationRepository.save(any(Notifications.class))).thenReturn(notif);
        when(mapper.toDTO(notif)).thenReturn(dto);

        NotificationsDTO result = notificationsService.createNotification(dto, companyId);

        assertEquals(1, result.getId());
        assertEquals(companyId, notif.getCompany().getId());
    }

    @Test
    void deleteNotification_WhenExists_Deletes() {
        Notifications notif = new Notifications();
        notif.setId(1);
        when(notificationRepository.findByIdAndCompanyId(1, companyId)).thenReturn(Optional.of(notif));

        notificationsService.deleteNotification(1, companyId);

        verify(notificationRepository).delete(notif);
    }

    @Test
    void deleteNotification_WhenNotExists_Throws() {
        when(notificationRepository.findByIdAndCompanyId(42, companyId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notificationsService.deleteNotification(42, companyId));

        assertEquals("Notification not found or not part of this company", ex.getMessage());
    }

    @Test
    void markAsRead_WhenExists_UpdatesAndReturnsDTO() {
        Notifications notif = new Notifications();
        notif.setId(1);
        notif.setIsRead(false);
        NotificationsDTO dto = new NotificationsDTO();
        dto.setId(1);
        dto.setIsRead(true);

        when(notificationRepository.findByIdAndCompanyId(1, companyId)).thenReturn(Optional.of(notif));
        when(notificationRepository.save(notif)).thenReturn(notif);
        when(mapper.toDTO(notif)).thenReturn(dto);

        NotificationsDTO result = notificationsService.markAsRead(1, companyId);

        assertTrue(notif.getIsRead());
        assertEquals(1, result.getId());
    }

    @Test
    void markAsRead_WhenNotExists_ReturnsNull() {
        when(notificationRepository.findByIdAndCompanyId(77, companyId)).thenReturn(Optional.empty());

        NotificationsDTO result = notificationsService.markAsRead(77, companyId);

        assertNull(result);
    }
}
