package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.VehicleDTO;
import com.ridesharingappbackend.service.VehicleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<VehicleDTO> getAllVehicles(
            @RequestParam(required = false) Integer companyId,
            HttpServletRequest request) {
        
        if (companyId == null) {
            companyId = (Integer) request.getAttribute("companyId");
        }
        return vehicleService.getAllVehicles(companyId);
    }

    @GetMapping("/company")
    public List<VehicleDTO> getVehiclesByCompany(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return vehicleService.getVehiclesByCompanyId(companyId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(
            @PathVariable Integer id,
            HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return vehicleService.getVehicleByIdAndCompanyId(id, companyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<VehicleDTO> getVehicleByDriverId(
            @PathVariable Integer driverId,
            HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        VehicleDTO vehicle = vehicleService.getVehicleByDriverId(driverId);
        if (vehicle != null && vehicle.getCompanyId().equals(companyId)) {
            return ResponseEntity.ok(vehicle);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    public VehicleDTO createVehicle(@RequestBody VehicleDTO vehicleDTO) {
        return vehicleService.createVehicle(vehicleDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<VehicleDTO> updateVehicle(
            @PathVariable Integer id,
            @RequestBody VehicleDTO vehicleDTO,
            HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        if (!vehicleService.existsByIdAndCompanyId(id, companyId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicleDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Integer id,
            HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        if (!vehicleService.existsByIdAndCompanyId(id, companyId)) {
            return ResponseEntity.notFound().build();
        }
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}