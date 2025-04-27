package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.models.RideTracking;
import com.ridesharingappbackend.service.RideTrackingService;
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
    public List<RideTracking> getAllRideTrackings() {
        return rideTrackingService.getAllRideTrackings();
    }

    @GetMapping("/{id}")
    public Optional<RideTracking> getRideTrackingById(@PathVariable Integer id) {
        return rideTrackingService.getRideTrackingById(id);
    }

    @PostMapping
    public RideTracking createRideTracking(@RequestBody RideTracking rideTracking) {
        return rideTrackingService.createRideTracking(rideTracking);
    }

    @DeleteMapping("/{id}")
    public boolean deleteRideTracking(@PathVariable Integer id) {
        return rideTrackingService.deleteRideTracking(id);
    }
}
