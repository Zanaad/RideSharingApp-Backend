package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RideTrackingDTO;
import com.ridesharingappbackend.service.RideTrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ride-tracking")
public class RideTrackingController {

    private final RideTrackingService rideTrackingService;

    public RideTrackingController(RideTrackingService rideTrackingService) {
        this.rideTrackingService = rideTrackingService;
    }

    @GetMapping
    public List<RideTrackingDTO> getAllRideTrackings() {
        return rideTrackingService.getAllRideTrackings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideTrackingDTO> getRideTrackingById(@PathVariable Integer id) {
        Optional<RideTrackingDTO> dto = rideTrackingService.getRideTrackingById(id);
        return dto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RideTrackingDTO createRideTracking(@RequestBody RideTrackingDTO dto) {
        return rideTrackingService.createRideTracking(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRideTracking(@PathVariable Integer id) {
        boolean deleted = rideTrackingService.deleteRideTracking(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
