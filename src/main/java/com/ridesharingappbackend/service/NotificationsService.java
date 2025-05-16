package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.NotificationsDTO;
import com.ridesharingappbackend.mappers.NotificationsMapper;
import com.ridesharingappbackend.models.Company;
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

    public List<NotificationsDTO> getAllNotifications(Integer companyId) {
        return notificationRepository.findByCompanyId(companyId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<NotificationsDTO> getNotificationById(Integer id, Integer companyId) {
        return notificationRepository.findByIdAndCompanyId(id, companyId)
                .map(mapper::toDTO);
    }

    public NotificationsDTO createNotification(NotificationsDTO dto, Integer companyId) {
        Notifications notification = mapper.toEntity(dto);
        Company company = new Company();
        company.setId(companyId);
        notification.setCompany(company);
        return mapper.toDTO(notificationRepository.save(notification));
    }

    public void deleteNotification(Integer id, Integer companyId) {
        Notifications notification = notificationRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
    }

    public NotificationsDTO markAsRead(Integer id, Integer companyId) {
        return notificationRepository.findByIdAndCompanyId(id, companyId)
                .map(notification -> {
                    notification.setIsRead(true);
                    return mapper.toDTO(notificationRepository.save(notification));
                })
                .orElse(null);
    }
}
