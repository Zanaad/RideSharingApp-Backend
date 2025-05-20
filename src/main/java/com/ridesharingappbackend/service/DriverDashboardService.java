package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.DriverDashboardDTO;
import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.dto.VehicleDTO;
import com.ridesharingappbackend.enums.RideStatus;
import com.ridesharingappbackend.enums.VehicleType;
import com.ridesharingappbackend.exceptions.ResourceNotFoundException;
import com.ridesharingappbackend.mappers.DriverDashboardMapper;
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


        private final DriverDashboardMapper driverDashboardMapper; // inject mapper here

        public DriverDashboardDTO getDashboardByUserId(Long userId) {
            Driver driver = driverRepository.findByUserId(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

            DriverDashboardDTO dto = driverDashboardMapper.toDTO(driver); // use mapper to convert


            return dto;
        }


    }




