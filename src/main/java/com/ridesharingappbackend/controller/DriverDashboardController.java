package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.DriverDashboardDTO;
import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.models.Driver;
import com.ridesharingappbackend.models.Ride;
import com.ridesharingappbackend.repository.DriverRepository;
import com.ridesharingappbackend.service.DriverDashboardService;
import com.ridesharingappbackend.service.RideService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dashboard")
public class DriverDashboardController {
    private DriverRepository driverRepository;
/*    private RideService r*/

    private final DriverDashboardService dashboardService;

    public DriverDashboardController(DriverDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }



}
