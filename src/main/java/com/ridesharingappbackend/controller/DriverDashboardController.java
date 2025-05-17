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


    @GetMapping
    public ResponseEntity<DriverDashboardDTO> getDashboard(@RequestParam Long userId) {
        DriverDashboardDTO dto = dashboardService.getDashboardByUserId(userId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}
