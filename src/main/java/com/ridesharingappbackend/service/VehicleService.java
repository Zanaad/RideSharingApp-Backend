package com.ridesharingappbackend.service;

import com.ridesharingappbackend.models.Vehicle;
import com.ridesharingappbackend.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleById(Integer id) {
        return vehicleRepository.findById(id);
    }

    public Vehicle getVehicleByDriverId(Integer driverId) {
        return vehicleRepository.findByDriverId(driverId);
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Integer id, Vehicle updatedVehicle) {
        return vehicleRepository.findById(id)
                .map(vehicle -> {
                    vehicle.setBrand(updatedVehicle.getBrand());
                    vehicle.setModel(updatedVehicle.getModel());
                    vehicle.setColor(updatedVehicle.getColor());
                    vehicle.setPlateNumber(updatedVehicle.getPlateNumber());
                    vehicle.setVehicleType(updatedVehicle.getVehicleType());
                    vehicle.setDriver(updatedVehicle.getDriver());
                    return vehicleRepository.save(vehicle);
                })
                .orElse(null);
    }

    public boolean deleteVehicle(Integer id) {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
