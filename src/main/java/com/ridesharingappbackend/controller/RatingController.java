package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RatingDTO;
import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.service.RatingService;
import com.ridesharingappbackend.service.RideService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;
    private final RideService rideService;

    public RatingController(RatingService ratingService, RideService rideService) {
        this.ratingService = ratingService;
        this.rideService=rideService;
    }

    @GetMapping
    public List<RatingDTO> getAllRatings(@RequestParam(required = false) Integer companyId, HttpServletRequest request) {
        if (companyId == null) {
            companyId = (Integer) request.getAttribute("companyId");
        }
        return ratingService.getAllRatings(companyId);
    }

    @GetMapping("/company")
    public List<RatingDTO> getRatingsByCompany(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ratingService.getAllRatings(companyId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingDTO> getRatingById(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ratingService.getRatingById(id, companyId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/driver/{driverId}")
    public List<RatingDTO> getRatingsByDriver(@PathVariable Integer driverId, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ratingService.getRatingsByCompanyAndDriverId(companyId, driverId);
    }

    @GetMapping("/ride/{rideId}")
    public ResponseEntity<RatingDTO> getRatingByRide(@PathVariable Integer rideId, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ratingService.getRatingByCompanyAndRideId(companyId, rideId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RatingDTO createRating(@RequestBody RatingDTO ratingDTO, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ratingService.createRating(ratingDTO, companyId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        if (ratingService.deleteRatingByIdAndCompanyId(id, companyId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/my")
    public ResponseEntity<List<RatingDTO>> getMyRatings(HttpServletRequest request) {
        Integer driverId = (Integer) request.getAttribute("userId");
        System.out.println("DriverId from request: " + driverId);

        if (driverId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<RatingDTO> ratings = ratingService.getRatingsForDriver(driverId);
        return ResponseEntity.ok(ratings);
    }

}
