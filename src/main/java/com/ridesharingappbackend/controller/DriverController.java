package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.DriverDTO;
import com.ridesharingappbackend.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * DriverController ofron API endpoint-e për menaxhimin e shoferëve (drivers)
 * të një kompanie të caktuar në sistemin e ride-sharing.
 *
 * Të gjitha metodat marrin `companyId` nga `HttpServletRequest` për të siguruar
 * izolim të të dhënave sipas kompanisë.
 *
 * Endpoint-et përfshijnë:
 * - Marrjen e të gjithë shoferëve
 * - Marrjen e një shoferi sipas ID-së
 * - Krijimin e një shoferi të ri
 * - Përditësimin e një shoferi ekzistues
 * - Fshirjen e një shoferi
 */
@RestController
@RequestMapping("/api/drivers")
@Tag(name = "Drivers", description = "Manage drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    /**
     * Kthen listën e të gjithë shoferëve që i përkasin kompanisë së kërkesës.
     *
     * @param request HttpServletRequest që përmban `companyId` si atribut.
     * @return lista e shoferëve të kompanisë, ose 500 INTERNAL_SERVER_ERROR në rast gabimi.
     */
    @Operation(summary = "Get all drivers for the company")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of drivers returned"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAllDrivers(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        try {
            List<DriverDTO> drivers = driverService.getAllDriversByCompany(companyId);

            return ResponseEntity.ok(drivers);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Gjen një shofer sipas ID-së brenda kompanisë së kërkesës.
     *
     * @param id ID-ja e shoferit që kërkohet.
     * @param request HttpServletRequest që përmban `companyId`.
     * @return shoferi nëse gjendet, ose 404 NOT_FOUND ose 500 INTERNAL_SERVER_ERROR në rast gabimi.
     */
    @Operation(summary = "Get a driver by ID for the company")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver returned"),
            @ApiResponse(responseCode = "404", description = "Driver not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");

        try {
            return driverService.getDriverById(id, companyId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        catch (RuntimeException e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Krijon një shofer të ri për kompaninë e kërkesës.
     *
     * @param dto objekti `DriverDTO` me të dhënat e shoferit për krijim.
     * @param request HttpServletRequest që përmban `companyId`.
     * @return shoferi i krijuar, ose 500 INTERNAL_SERVER_ERROR në rast gabimi.
     */
    @Operation(summary = "Create a new driver for the company")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver created successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(@RequestBody DriverDTO dto, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        try {
            DriverDTO created = driverService.createDriver(dto, companyId);
            return ResponseEntity.ok(created);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Përditëson të dhënat e një shoferi ekzistues sipas ID-së dhe kompanisë.
     *
     * @param id ID-ja e shoferit që do të përditësohet.
     * @param dto objekt `DriverDTO` me të dhënat e reja.
     * @param request HttpServletRequest që përmban `companyId`.
     * @return shoferi i përditësuar, ose 404 NOT_FOUND nëse nuk ekziston, ose 500 INTERNAL_SERVER_ERROR në rast gabimi.
     */
    @Operation(summary = "Update an existing driver by ID for the company")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver updated successfully"),
            @ApiResponse(responseCode = "404", description = "Driver not found"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriver(@PathVariable Integer id, @RequestBody DriverDTO dto, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        try {
            DriverDTO updated = driverService.updateDriver(id, dto, companyId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Fshin një shofer sipas ID-së dhe `companyId` të kërkesës.
     *
     * @param id ID-ja e shoferit që do të fshihet.
     * @param request HttpServletRequest që përmban `companyId`.
     * @return status 204 NO_CONTENT nëse suksesshëm, ose 404 NOT_FOUND nëse nuk ekziston.
     */
    @Operation(summary = "Delete a driver by ID for the company")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Driver deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Driver not found"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        try {
            driverService.deleteDriver(id, companyId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
