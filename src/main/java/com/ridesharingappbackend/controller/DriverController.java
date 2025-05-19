package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.DriverDTO;
import com.ridesharingappbackend.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }


    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAllDrivers(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        try {
            List<DriverDTO> drivers = driverService.getAllDriversByCompany(companyId);

            return ResponseEntity.ok(drivers);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");

        try {
            return driverService.getDriverById(id, companyId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        catch (RuntimeException e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(@RequestBody DriverDTO dto, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        try {
            DriverDTO created = driverService.createDriver(dto, companyId);
            return ResponseEntity.ok(created);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriver(@PathVariable Integer id, @RequestBody DriverDTO dto, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        try {
            DriverDTO updated = driverService.updateDriver(id, dto, companyId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        try {
            driverService.deleteDriver(id, companyId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
