package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.DriverDashboardDTO;
import com.ridesharingappbackend.service.DriverDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DriverDashboardController {

    private final DriverDashboardService dashboardService;

    public DriverDashboardController(DriverDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<DriverDashboardDTO> getDriverDashboard(@PathVariable Integer driverId) {
        try {
            DriverDashboardDTO dto = dashboardService.getDashboardData(driverId);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
