package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.DriverDashboardDTO;
import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.dto.VehicleDTO;
import com.ridesharingappbackend.enums.RideStatus;
import com.ridesharingappbackend.enums.VehicleType;
import com.ridesharingappbackend.exceptions.ResourceNotFoundException;
import com.ridesharingappbackend.models.Driver;
import com.ridesharingappbackend.models.DriverLicense;
import com.ridesharingappbackend.models.Ride;
import com.ridesharingappbackend.models.Vehicle;
import com.ridesharingappbackend.repository.DriverLicenseRepository;
import com.ridesharingappbackend.repository.DriverRepository;
import com.ridesharingappbackend.repository.RideRepository;
import com.ridesharingappbackend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 import lombok.Builder;
 import lombok.Data;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DriverDashboardService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public DriverDashboardDTO getDashboardByUserId(Long userId) {
        // Merr drejtuesin përmes userId
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        // Numri i udhëtimeve të përfunduara

        // Fitimet totale


        // Udhëtimi aktual nëse ekziston


        // Automjeti i lidhur me shoferin
        VehicleDTO vehicle = null;
        Vehicle vehicleEntity = vehicleRepository.findByDriverId(driver.getId());
        if (vehicleEntity != null) {
            vehicle = convertToVehicleDTO(vehicleEntity);
        }

        // Emri i plotë i shoferit
        String fullName = driver.getUser().getFirstName() + " " + driver.getUser().getLastName();

        // Emri i kompanisë (nëse ka)
        String companyName = driver.getCompany() != null ? driver.getCompany().getName() : null;

        // Kthimi i të dhënave në DTO
        return new DriverDashboardDTO(
                driver.getId(),
                fullName,
                driver.getRating(),
                driver.getStatus()
             /*   driver.getLicenseNumber()*/
        );

    }

    // Konvertimi i Ride në DTO
    private RideDTO convertToRideDTO(Ride ride) {
        RideDTO dto = new RideDTO();
        dto.setId(ride.getId());
        dto.setStatus(RideStatus.COMPLETED);
        dto.setPickupLocation(ride.getPickupLocation());
        dto.setDropOffLocation(ride.getDropOffLocation());
        dto.setPickupTime(ride.getPickupTime());
        return dto;
    }

    // Konvertimi i Vehicle në DTO
    private VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setBrand(vehicle.getBrand());
        dto.setColor(vehicle.getColor());
        dto.setModel(vehicle.getModel());
        dto.setPlateNumber(vehicle.getPlateNumber());
        dto.setVehicleType(VehicleType.MOTORBIKE);
        return dto;
    }
}



