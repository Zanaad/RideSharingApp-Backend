package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.VehicleDTO;
import com.ridesharingappbackend.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 *
 */
@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicles", description = "APIs for managing vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    @Operation(summary = "Get all vehicles for the company", description = "Returns a list of all vehicles belonging to the company of the authenticated user")
    @ApiResponse(responseCode = "200", description = "List of vehicles retrieved successfully")
    public List<VehicleDTO> getAllVehicles(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return vehicleService.getAllVehiclesByCompany(companyId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a vehicle by ID", description = "Returns vehicle details by vehicle ID if it belongs to the user's company")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle found"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found or not accessible")
    })
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return vehicleService.getVehicleByIdAndCompanyId(id, companyId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get vehicle by driver ID", description = "Returns the vehicle assigned to the specified driver in the user's company")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle found for driver"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found for driver or not accessible")
    })
    public ResponseEntity<VehicleDTO> getVehicleByDriverId(@PathVariable Integer driverId, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return vehicleService.getVehicleByDriverIdAndCompanyId(driverId, companyId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new vehicle", description = "Creates a new vehicle record under the user's company")
    @ApiResponse(responseCode = "200", description = "Vehicle created successfully")
    public ResponseEntity<VehicleDTO> createVehicle(@RequestBody VehicleDTO vehicleDTO, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        vehicleDTO.setCompanyId(companyId);
        VehicleDTO created = vehicleService.createVehicle(vehicleDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing vehicle", description = "Updates the vehicle identified by ID if it belongs to the user's company")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle updated successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found or not accessible")
    })
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Integer id, @RequestBody VehicleDTO vehicleDTO, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ResponseEntity.ok(vehicleService.updateVehicle(id, companyId, vehicleDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vehicle", description = "Deletes the vehicle identified by ID if it belongs to the user's company")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vehicle deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found or not accessible")
    })
    public ResponseEntity<Void> deleteVehicle(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        boolean deleted = vehicleService.deleteVehicle(id, companyId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}