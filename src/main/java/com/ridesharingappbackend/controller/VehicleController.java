package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.VehicleDTO;
import com.ridesharingappbackend.service.VehicleService;
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
    public List<VehicleDTO> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}")
    public Optional<VehicleDTO> getVehicleById(@PathVariable Integer id) {
        return vehicleService.getVehicleById(id);
    }

    @GetMapping("/driver/{driverId}")
    public VehicleDTO getVehicleByDriverId(@PathVariable Integer driverId) {
        return vehicleService.getVehicleByDriverId(driverId);
    }

    @PostMapping
    public VehicleDTO createVehicle(@RequestBody VehicleDTO vehicleDTO) {
        return vehicleService.createVehicle(vehicleDTO);
    }

    @PutMapping("/{id}")
    public VehicleDTO updateVehicle(@PathVariable Integer id, @RequestBody VehicleDTO vehicleDTO) {
        return vehicleService.updateVehicle(id, vehicleDTO);
    }

    @DeleteMapping("/{id}")
    public boolean deleteVehicle(@PathVariable Integer id) {
        return vehicleService.deleteVehicle(id);
    }
}
