package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.NotificationsDTO;
import com.ridesharingappbackend.mappers.NotificationsMapper;
import com.ridesharingappbackend.models.Notifications;
import com.ridesharingappbackend.repository.NotificationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationsService {

    private final NotificationsRepository notificationRepository;
    private final NotificationsMapper mapper;

    public NotificationsService(NotificationsRepository notificationRepository, NotificationsMapper mapper) {
        this.notificationRepository = notificationRepository;
        this.mapper = mapper;
    }

    public List<NotificationsDTO> getAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<NotificationsDTO> getNotificationById(Integer id) {
        return notificationRepository.findById(id)
                .map(mapper::toDTO);
    }

    public List<NotificationsDTO> getNotificationsByUserId(Integer userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public NotificationsDTO createNotification(NotificationsDTO dto) {
        Notifications notification = mapper.toEntity(dto);
        return mapper.toDTO(notificationRepository.save(notification));
    }

    public void deleteNotification(Integer id) {
        notificationRepository.deleteById(id);
    }

    public NotificationsDTO markAsRead(Integer id) {
        return notificationRepository.findById(id)
                .map(notification -> {
                    notification.setIsRead(true);
                    return mapper.toDTO(notificationRepository.save(notification));
                })
                .orElse(null);
    }
}
