package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.enums.RideStatus;
import com.ridesharingappbackend.service.RideService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping("/company")
    public List<RideDTO> getRidesByCompany(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByCompany(companyId);
    }

    @GetMapping("/company/status/{status}")
    public List<RideDTO> getRidesByCompanyAndStatus(
            HttpServletRequest request,
            @PathVariable RideStatus status) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByCompanyAndStatus(companyId, status);
    }

    @GetMapping("/company/driver/{driverId}")
    public List<RideDTO> getRidesByCompanyAndDriver(
            HttpServletRequest request,
            @PathVariable Integer driverId) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByCompanyAndDriver(companyId, driverId);
    }

    @GetMapping("/company/passenger/{passengerId}")
    public List<RideDTO> getRidesByCompanyAndPassenger(
            HttpServletRequest request,
            @PathVariable Integer passengerId) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByCompanyAndPassenger(companyId, passengerId);
    }

    @PostMapping
    public RideDTO createRide(@RequestBody RideDTO rideDTO) {
        return rideService.createRide(rideDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<Void> deleteRide(@PathVariable Integer id) {
        boolean deleted = rideService.deleteRide(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}