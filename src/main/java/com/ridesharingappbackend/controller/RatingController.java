package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RatingDTO;
import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.service.RatingService;
import com.ridesharingappbackend.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontrolleri REST për menaxhimin e vlerësimeve (ratings) në aplikacionin për ndarjen e udhëtimeve.
 *
 * Kjo klasë ofron pika fundore (endpoints) për të:
 * <ul>
 *     <li>Marrë të gjitha vlerësimet për një kompani specifike</li>
 *     <li>Marrë një vlerësim të vetëm sipas ID-së dhe kompanisë</li>
 *     <li>Marrë të gjitha vlerësimet për një shofer të caktuar</li>
 *     <li>Marrë një vlerësim sipas ID-së së udhëtimit</li>
 *     <li>Krijuar një vlerësim të ri</li>
 *     <li>Fshirë një vlerësim ekzistues</li>
 *     <li>Marrë të gjitha vlerësimet për shoferin e identifikuar (vetveten)</li>
 * </ul>
 *
 * {@code companyId} dhe {@code userId} nxirren nga atributet e {@link HttpServletRequest}, duke garantuar
 * se operacionet janë të kufizuara brenda kontekstit të kompanisë përkatëse dhe përdoruesit të identifikuar.
 *
 * Delegon logjikën e biznesit tek {@link RatingService} dhe përdor gjithashtu {@link RideService} për referencë.
 *

 */

@RestController
@RequestMapping("/api/ratings")
@Tag(name = "Ratings", description = "Operations related to ride ratings")
public class RatingController {

    private final RatingService ratingService;
    private final RideService rideService;

    public RatingController(RatingService ratingService, RideService rideService) {
        this.ratingService = ratingService;
        this.rideService=rideService;
    }
    @Operation(summary = "Get all ratings for a company")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ratings fetched successfully")
    })
    @GetMapping
    public List<RatingDTO> getAllRatings(@RequestParam(required = false) Integer companyId, HttpServletRequest request) {
        if (companyId == null) {
            companyId = (Integer) request.getAttribute("companyId");
        }
        return ratingService.getAllRatings(companyId);
    }
    @Operation(summary = "Get ratings by company from request context")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Company ratings fetched successfully")
    })
    @GetMapping("/company")
    public List<RatingDTO> getRatingsByCompany(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ratingService.getAllRatings(companyId);
    }
    @Operation(summary = "Get a rating by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rating found"),
            @ApiResponse(responseCode = "404", description = "Rating not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RatingDTO> getRatingById(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ratingService.getRatingById(id, companyId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Get all ratings for a specific driver")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver ratings fetched")
    })
    @GetMapping("/driver/{driverId}")
    public List<RatingDTO> getRatingsByDriver(@PathVariable Integer driverId, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ratingService.getRatingsByCompanyAndDriverId(companyId, driverId);
    }
    @Operation(summary = "Get rating by ride ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rating for ride found"),
            @ApiResponse(responseCode = "404", description = "Rating not found")
    })
    @GetMapping("/ride/{rideId}")
    public ResponseEntity<RatingDTO> getRatingByRide(@PathVariable Integer rideId, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ratingService.getRatingByCompanyAndRideId(companyId, rideId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Create a new rating")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Rating created successfully")
    })
    @PostMapping
    public RatingDTO createRating(@RequestBody RatingDTO ratingDTO, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ratingService.createRating(ratingDTO, companyId);
    }
    @Operation(summary = "Delete a rating by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Rating deleted"),
            @ApiResponse(responseCode = "404", description = "Rating not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        if (ratingService.deleteRatingByIdAndCompanyId(id, companyId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @Operation(summary = "Get ratings submitted for the logged-in driver")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver's ratings fetched"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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
