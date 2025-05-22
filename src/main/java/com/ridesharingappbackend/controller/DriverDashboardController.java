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

/**
 * Kontrolleri REST për menaxhimin e dashboard-it të shoferit në aplikacionin e ndarjes së udhëtimeve.
 *
 * Ky kontroller është përgjegjës për trajtimin e kërkesave HTTP që lidhen me dashboard-in
 * e shoferit, duke përfshirë statistikat, udhëtimet aktive, historikun dhe të dhëna të tjera
 * të personalizuara që shfaqen në panelin e tij informues.
 *
 * URL bazë për kërkesat e këtij kontrolleri është: /api/dashboard
 *
 * Përdor {@link DriverDashboardService} për të përpunuar logjikën e biznesit.
 *
 * @author [Hare Luma]
 * @version 1.0
 * @since 2025-05-22
 */
@RestController
@RequestMapping("/api/dashboard")
public class DriverDashboardController {
    private DriverRepository driverRepository;

    private final DriverDashboardService dashboardService;

    /**
     * Konstruktor për të inicializuar kontrollerin me shërbimin e dashboard-it të shoferit.
     *
     * @param dashboardService shërbimi që menaxhon logjikën e dashboard-it të shoferit
     */
    public DriverDashboardController(DriverDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // Endpoints do shtohen këtu për të trajtuar kërkesa specifike për dashboard-in
}

