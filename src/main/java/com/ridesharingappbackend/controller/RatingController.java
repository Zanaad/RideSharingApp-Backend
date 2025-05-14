package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RatingDTO;
import com.ridesharingappbackend.service.RatingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public List<RatingDTO> getAllRatings(
            @RequestParam(required = false) Integer companyId,
            HttpServletRequest request) {
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
    public ResponseEntity<RatingDTO> getRatingById(
            @PathVariable Integer id,
            HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ratingService.getRatingByIdAndCompanyId(id, companyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/driver/{driverId}")
    public List<RatingDTO> getRatingsByDriver(
            @PathVariable Integer driverId,
            HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ratingService.getRatingsByCompanyAndDriverId(companyId, driverId);
    }

    @GetMapping("/ride/{rideId}")
    public ResponseEntity<RatingDTO> getRatingByRide(
            @PathVariable Integer rideId,
            HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ratingService.getRatingByCompanyAndRideId(companyId, rideId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN', 'USER')")
    public RatingDTO createRating(@RequestBody RatingDTO ratingDTO) {
        return ratingService.createRating(ratingDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<Void> deleteRating(
            @PathVariable Integer id,
            HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        if (ratingService.deleteRatingByIdAndCompanyId(id, companyId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}