package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RatingDTO;
import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.enums.RideStatus;
import com.ridesharingappbackend.service.RatingService;
import com.ridesharingappbackend.service.RideService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;
    private final RatingService ratingService;

    public RideController(RideService rideService, RatingService ratingService) {
        this.rideService = rideService;
        this.ratingService=ratingService;
    }

    @GetMapping
    public List<RideDTO> getAllRides(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByCompany(companyId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideDTO> getRideById(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRideById(id, companyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/passenger/{passengerId}")
    public List<RideDTO> getRidesByPassengerId(@PathVariable Integer passengerId, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByCompanyAndPassenger(companyId, passengerId);
    }

    @GetMapping("/company")
    public List<RideDTO> getRidesByCompany(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByCompany(companyId);
    }

    @GetMapping("/company/status/{status}")
    public List<RideDTO> getRidesByCompanyAndStatus(@PathVariable RideStatus status, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByCompanyAndStatus(companyId, status);
    }

    @GetMapping("/company/driver/{driverId}")
    public List<RideDTO> getRidesByCompanyAndDriver(@PathVariable Integer driverId, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByCompanyAndDriver(companyId, driverId);
    }

    @GetMapping("/company/passenger/{passengerId}")
    public List<RideDTO> getRidesByCompanyAndPassenger(@PathVariable Integer passengerId, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByCompanyAndPassenger(companyId, passengerId);
    }

    @PostMapping
    public ResponseEntity<RideDTO> bookRide(@RequestBody RideDTO rideDTO, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        Integer passengerId = (Integer) request.getAttribute("userId");

        if (companyId == null || passengerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        RideDTO createdRide = rideService.createRide(rideDTO, companyId, passengerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRide);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        boolean deleted = rideService.deleteRide(id, companyId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @GetMapping("/my")
    public ResponseEntity<List<RideDTO>> getMyRideHistory(HttpServletRequest request) {
        Integer driverId = (int) request.getAttribute("userId");
        System.out.println("DriverId from request: " + driverId);  // Kontroll

        if (driverId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<RideDTO> rides = rideService.getRidesByDriver(driverId);
        return ResponseEntity.ok(rides);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateRideStatus(
            @PathVariable Integer id,
            @RequestBody RideDTO statusUpdateDTO,
            HttpServletRequest request) {

        Integer userId = (int) request.getAttribute("userId");

        if (statusUpdateDTO.getStatus() == null) {
            return ResponseEntity.badRequest().body("Invalid status");
        }

        boolean updated = rideService.updateRideStatus(id, userId, statusUpdateDTO.getStatus());

        if (updated) {
            return ResponseEntity.ok("Ride status updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not update ride status");
        }


}