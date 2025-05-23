package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RatingDTO;
import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.enums.RideStatus;
import com.ridesharingappbackend.service.RatingService;
import com.ridesharingappbackend.service.RideService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RideController ofron endpoint-e REST për menaxhimin e udhëtimeve (rides) në aplikacionin e ndarjes së udhëtimeve.
 *
 * Përfshin funksionalitete për:
 * - Listimin e udhëtimeve sipas kompanisë, statusit apo drejtuesit
 * - Shfaqjen e historikut personal për pasagjerët dhe drejtuesit
 * - Krijimin, fshirjen dhe përditësimin e statusit të një udhëtimi
 */
@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;
    private final RatingService ratingService;

    public RideController(RideService rideService, RatingService ratingService) {
        this.rideService = rideService;
        this.ratingService=ratingService;
    }

    /**
     * Kthen të gjitha udhëtimet për kompaninë e lidhur me kërkesën.
     *
     * @param request objekti HttpServletRequest që përmban atributin "companyId".
     * @return lista e të gjitha udhëtimeve për kompaninë.
     */
    @GetMapping
    public List<RideDTO> getAllRides(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByCompany(companyId);
    }

    /**
     * Kthen një udhëtim sipas ID-së brenda një kompanie specifike.
     *
     * @param id ID e udhëtimit.
     * @param request objekti që përmban "companyId".
     * @return udhëtimi nëse ekziston, përndryshe 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RideDTO> getRideById(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRideById(id, companyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Kthen të gjitha udhëtimet e pasagjerit të kyçur.
     *
     * @param request përmban "userId" dhe "companyId".
     * @return lista e udhëtimeve të pasagjerit.
     */
    @GetMapping("/my-rides")
    public List<RideDTO> getMyRides(HttpServletRequest request) {
        Integer passengerId = (Integer) request.getAttribute("userId");
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByPassengerId(passengerId, companyId);
    }

    /**
     * Kthen të gjitha udhëtimet për kompaninë nga e cila bëhet kërkesa.
     *
     * @param request që përmban "companyId".
     * @return lista e udhëtimeve të kompanisë.
     */
    @GetMapping("/company")
    public List<RideDTO> getRidesByCompany(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByCompany(companyId);
    }

    /**
     * Kthen udhëtimet e kompanisë të filtruar sipas statusit (p.sh. COMPLETED, PENDING).
     *
     * @param status statusi i udhëtimit.
     * @param request që përmban "companyId".
     * @return lista e udhëtimeve me statusin e caktuar.
     */
    @GetMapping("/company/status/{status}")
    public List<RideDTO> getRidesByCompanyAndStatus(@PathVariable RideStatus status, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByCompanyAndStatus(companyId, status);
    }

    /**
     * Kthen të gjitha udhëtimet e një drejtuesi të caktuar brenda një kompanie.
     *
     * @param driverId ID e drejtuesit.
     * @param request që përmban "companyId".
     * @return lista e udhëtimeve për drejtuesin në fjalë.
     */
    @GetMapping("/company/driver/{driverId}")
    public List<RideDTO> getRidesByCompanyAndDriver(@PathVariable Integer driverId, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return rideService.getRidesByCompanyAndDriver(companyId, driverId);
    }

    /**
     * Krijon një udhëtim të ri për pasagjerin e kyçur në kompaninë e specifikuar.
     *
     * @param rideDTO të dhënat e udhëtimit.
     * @param request që përmban "companyId" dhe "userId".
     * @return udhëtimi i sapo krijuar.
     */
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

    /**
     * Fshin një udhëtim të caktuar nëse ekziston për kompaninë përkatëse.
     *
     * @param id ID e udhëtimit për të fshirë.
     * @param request që përmban "companyId".
     * @return 204 nëse fshihet me sukses, 404 nëse nuk gjendet.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        boolean deleted = rideService.deleteRide(id, companyId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Kthen historikun e udhëtimeve të drejtuesit të kyçur.
     *
     * @param request që përmban "userId" (ID e drejtuesit).
     * @return lista e udhëtimeve që ka kryer drejtuesi.
     */
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

    /**
     * Përditëson statusin e një udhëtimi (p.sh. në 'IN_PROGRESS' ose 'COMPLETED').
     *
     * @param id ID e udhëtimit që do përditësohet.
     * @param statusUpdateDTO DTO që përmban statusin e ri.
     * @param request që përmban "userId" (ID e përdoruesit që e përditëson).
     * @return mesazh suksesi ose gabimi.
     */
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

}