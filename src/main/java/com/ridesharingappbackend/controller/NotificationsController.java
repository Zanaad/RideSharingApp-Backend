package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.NotificationsDTO;
import com.ridesharingappbackend.service.NotificationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * NotificationsController ofron endpoint-e REST për menaxhimin e njoftimeve (notifications)
 * që i përkasin një kompanie të caktuar.
 *
 * Të gjitha operacionet kërkojnë ID-në e kompanisë si parametër (`companyId`)
 * për të garantuar që njoftimet janë të izoluara sipas kompanisë.
 *
 * Endpoint-et përfshijnë:
 * - Listimin e të gjitha njoftimeve
 * - Gjetjen e një njoftimi sipas ID-së
 * - Krijimin e një njoftimi të ri
 * - Fshirjen e një njoftimi ekzistues
 * - Shënimin e një njoftimi si të lexuar
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationsController {

    private final NotificationsService notificationService;

    public NotificationsController(NotificationsService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Kthen listën e të gjitha njoftimeve për një kompani të caktuar.
     *
     * @param companyId ID-ja e kompanisë për të cilën kërkohen njoftimet.
     * @return lista e njoftimeve në formën e DTO-ve.
     */
    @GetMapping
    public List<NotificationsDTO> getAllNotifications(@RequestParam Integer companyId) {
        return notificationService.getAllNotifications(companyId);
    }

    /**
     * Kthen një njoftim sipas ID-së për një kompani të caktuar.
     *
     * @param id ID-ja e njoftimit.
     * @param companyId ID-ja e kompanisë që posedon njoftimin.
     * @return objekti NotificationsDTO nëse gjendet, përndryshe 404 NOT_FOUND.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationsDTO> getNotificationById(
            @PathVariable Integer id,
            @RequestParam Integer companyId) {
        Optional<NotificationsDTO> notification = notificationService.getNotificationById(id, companyId);
        return notification.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Krijon një njoftim të ri për një kompani të caktuar.
     *
     * @param dto objekti DTO që përmban të dhënat e njoftimit.
     * @param companyId ID-ja e kompanisë që do ta zotërojë njoftimin.
     * @return objekti NotificationsDTO i krijuar.
     */
    @PostMapping
    public NotificationsDTO createNotification(
            @RequestBody NotificationsDTO dto,
            @RequestParam Integer companyId) {
        return notificationService.createNotification(dto, companyId);
    }

    /**
     * Fshin një njoftim specifik për një kompani të caktuar.
     *
     * @param id ID-ja e njoftimit që do të fshihet.
     * @param companyId ID-ja e kompanisë që posedon njoftimin.
     * @return 204 NO_CONTENT nëse fshihet me sukses.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Integer id,
            @RequestParam Integer companyId) {
        notificationService.deleteNotification(id, companyId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Shenon një njoftim si të lexuar për një kompani të caktuar.
     *
     * @param id ID-ja e njoftimit që do të shënohet si i lexuar.
     * @param companyId ID-ja e kompanisë që posedon njoftimin.
     * @return objekti i përditësuar NotificationsDTO nëse ekziston, përndryshe 404 NOT_FOUND.
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationsDTO> markAsRead(
            @PathVariable Integer id,
            @RequestParam Integer companyId) {
        NotificationsDTO updated = notificationService.markAsRead(id, companyId);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
}
