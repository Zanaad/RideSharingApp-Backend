package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @GetMapping
    public List<RideDTO> getAllRides() {
        return rideService.getAllRides();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideDTO> getRideById(@PathVariable Integer id) {
        Optional<RideDTO> ride = rideService.getRideById(id);
        return ride.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/passenger/{passengerId}")
    public List<RideDTO> getRidesByPassengerId(@PathVariable Integer passengerId) {
        return rideService.getRidesByPassengerId(passengerId);
    }

    @PostMapping
    public RideDTO createRide(@RequestBody RideDTO rideDTO) {
        return rideService.createRide(rideDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Integer id) {
        boolean deleted = rideService.deleteRide(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
