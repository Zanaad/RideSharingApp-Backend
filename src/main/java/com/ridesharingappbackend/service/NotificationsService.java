package com.ridesharingappbackend.service;

import com.ridesharingappbackend.models.Notifications;
import com.ridesharingappbackend.repository.NotificationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationsService {

    private final NotificationsRepository notificationRepository;

    public NotificationsService(NotificationsRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notifications> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Optional<Notifications> getNotificationById(Integer id) {
        return notificationRepository.findById(id);
    }

    public List<Notifications> getNotificationsByUserId(Integer userId) {
        return notificationRepository.findByUserId(userId);
    }

    public Notifications createNotification(Notifications notification) {
        return notificationRepository.save(notification);
    }

    public void deleteNotification(Integer id) {
        notificationRepository.deleteById(id);
    }

    public Notifications markAsRead(Integer id) {
        return notificationRepository.findById(id)
                .map(notification -> {
                    notification.setIsRead(true);
                    return notificationRepository.save(notification);
                }).orElse(null);
    }
}

