package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.models.Vehicle;
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
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}")
    public Optional<Vehicle> getVehicleById(@PathVariable Integer id) {
        return vehicleService.getVehicleById(id);
    }

    @GetMapping("/driver/{driverId}")
    public Vehicle getVehicleByDriverId(@PathVariable Integer driverId) {
        return vehicleService.getVehicleByDriverId(driverId);
    }

    @PostMapping
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.createVehicle(vehicle);
    }

    @PutMapping("/{id}")
    public Vehicle updateVehicle(@PathVariable Integer id, @RequestBody Vehicle vehicle) {
        return vehicleService.updateVehicle(id, vehicle);
    }

    @DeleteMapping("/{id}")
    public boolean deleteVehicle(@PathVariable Integer id) {
        return vehicleService.deleteVehicle(id);
    }
}
