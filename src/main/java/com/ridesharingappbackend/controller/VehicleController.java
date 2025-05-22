package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.VehicleDTO;
import com.ridesharingappbackend.service.VehicleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroller për menaxhimin e automjeteve në aplikacionin e ndarjes së udhëtimeve.
 *
 * Ky kontroller ekspozon endpoint-e REST për të kryer operacione CRUD mbi automjetet
 * që i përkasin një kompanie specifike, e identifikuar përmes {@code companyId}
 * të vendosur në kontekstin e kërkesës.
 *
 * Endpoint-et e ofruara përfshijnë:
 * <ul>
 *   <li>{@code GET /api/vehicles} - Merr të gjitha automjetet e një kompanie</li>
 *   <li>{@code GET /api/vehicles/{id}} - Merr një automjet sipas ID-së</li>
 *   <li>{@code GET /api/vehicles/driver/{driverId}} - Merr automjetin e lidhur me një shofer</li>
 *   <li>{@code POST /api/vehicles} - Krijon një automjet të ri për kompaninë</li>
 *   <li>{@code PUT /api/vehicles/{id}} - Përditëson të dhënat e një automjeti ekzistues</li>
 *   <li>{@code DELETE /api/vehicles/{id}} - Fshin një automjet sipas ID-së</li>
 * </ul>
 *
 * {@link VehicleService} përdoret për të deleguar logjikën e biznesit.
 * Të gjitha operacionet janë të mbrojtura dhe të kufizuara sipas {@code companyId}.
 *
 * @author [Emri juaj]
 * @version 1.0
 * @since 2025-05-22
 */
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<VehicleDTO> getAllVehicles(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return vehicleService.getAllVehiclesByCompany(companyId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return vehicleService.getVehicleByIdAndCompanyId(id, companyId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<VehicleDTO> getVehicleByDriverId(@PathVariable Integer driverId, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return vehicleService.getVehicleByDriverIdAndCompanyId(driverId, companyId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VehicleDTO> createVehicle(@RequestBody VehicleDTO vehicleDTO, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        vehicleDTO.setCompanyId(companyId);
        VehicleDTO created = vehicleService.createVehicle(vehicleDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Integer id, @RequestBody VehicleDTO vehicleDTO, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ResponseEntity.ok(vehicleService.updateVehicle(id, companyId, vehicleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        boolean deleted = vehicleService.deleteVehicle(id, companyId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
