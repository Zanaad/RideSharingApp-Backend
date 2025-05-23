package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.NotificationsDTO;
import com.ridesharingappbackend.service.NotificationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationsController {

    private final NotificationsService notificationService;

    public NotificationsController(NotificationsService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationsDTO> getAllNotifications(@RequestParam Integer companyId) {
        return notificationService.getAllNotifications(companyId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationsDTO> getNotificationById(
            @PathVariable Integer id,
            @RequestParam Integer companyId) {
        Optional<NotificationsDTO> notification = notificationService.getNotificationById(id, companyId);
        return notification.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public NotificationsDTO createNotification(
            @RequestBody NotificationsDTO dto,
            @RequestParam Integer companyId) {
        return notificationService.createNotification(dto, companyId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Integer id,
            @RequestParam Integer companyId) {
        notificationService.deleteNotification(id, companyId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationsDTO> markAsRead(
            @PathVariable Integer id,
            @RequestParam Integer companyId) {
        NotificationsDTO updated = notificationService.markAsRead(id, companyId);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user")
    public List<NotificationsDTO> getNotificationsByUser(
            @RequestParam Integer userId,
            @RequestParam Integer companyId) {
        return notificationService.getNotificationsByUser(userId, companyId);
    }

    @GetMapping("/user/unread")
    public List<NotificationsDTO> getUnreadNotificationsByUser(
            @RequestParam Integer userId,
            @RequestParam Integer companyId) {
        return notificationService.getUnreadNotificationsByUser(userId, companyId);
    }

}
