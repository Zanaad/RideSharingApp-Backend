package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.VehicleDTO;
import com.ridesharingappbackend.enums.VehicleType;
import com.ridesharingappbackend.service.VehicleService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleControllerTest {

    @InjectMocks
    private VehicleController vehicleController;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllVehicles_returnsList() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        VehicleDTO vehicle1 = new VehicleDTO(1, 1, VehicleType.SEDAN, "Toyota", "Corolla", "ABC123", "Red", 1);
        VehicleDTO vehicle2 = new VehicleDTO(2, 2, VehicleType.MOTORBIKE, "Honda", "CBR", "XYZ789", "Black", 1);

        when(vehicleService.getAllVehiclesByCompany(1)).thenReturn(List.of(vehicle1, vehicle2));

        List<VehicleDTO> result = vehicleController.getAllVehicles(httpServletRequest);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Toyota", result.get(0).getBrand());
    }

    @Test
    void getVehicleById_existingVehicle_returnsVehicle() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        VehicleDTO vehicle = new VehicleDTO(1, 1, VehicleType.SEDAN, "Toyota", "Corolla", "ABC123", "Red", 1);

        when(vehicleService.getVehicleByIdAndCompanyId(1, 1)).thenReturn(Optional.of(vehicle));

        ResponseEntity<VehicleDTO> response = vehicleController.getVehicleById(1, httpServletRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.hasBody());
        assertEquals("Toyota", response.getBody().getBrand());
    }

    @Test
    void getVehicleById_vehicleNotFound_returnsNotFound() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        when(vehicleService.getVehicleByIdAndCompanyId(1, 1)).thenReturn(Optional.empty());

        ResponseEntity<VehicleDTO> response = vehicleController.getVehicleById(1, httpServletRequest);

        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.hasBody());
    }

    @Test
    void createVehicle_success() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        VehicleDTO vehicleToCreate = new VehicleDTO(null, 1, VehicleType.SEDAN, "Toyota", "Corolla", "ABC123", "Red", null);
        VehicleDTO createdVehicle = new VehicleDTO(1, 1, VehicleType.SEDAN, "Toyota", "Corolla", "ABC123", "Red", 1);

        when(vehicleService.createVehicle(any(VehicleDTO.class))).thenReturn(createdVehicle);

        ResponseEntity<VehicleDTO> response = vehicleController.createVehicle(vehicleToCreate, httpServletRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        assertEquals(1, response.getBody().getCompanyId());
    }

    @Test
    void updateVehicle_success() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        VehicleDTO vehicleToUpdate = new VehicleDTO(null, 1, VehicleType.SEDAN, "Toyota", "Corolla", "ABC123", "Red", null);
        VehicleDTO updatedVehicle = new VehicleDTO(1, 1, VehicleType.SEDAN, "Toyota", "Corolla", "ABC123", "Red", 1);

        when(vehicleService.updateVehicle(1, 1, vehicleToUpdate)).thenReturn(updatedVehicle);

        ResponseEntity<VehicleDTO> response = vehicleController.updateVehicle(1, vehicleToUpdate, httpServletRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getId());
        assertEquals("Toyota", response.getBody().getBrand());
    }

    @Test
    void deleteVehicle_success() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        when(vehicleService.deleteVehicle(1, 1)).thenReturn(true);

        ResponseEntity<Void> response = vehicleController.deleteVehicle(1, httpServletRequest);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void deleteVehicle_notFound() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        when(vehicleService.deleteVehicle(1, 1)).thenReturn(false);

        ResponseEntity<Void> response = vehicleController.deleteVehicle(1, httpServletRequest);

        assertEquals(404, response.getStatusCodeValue());
    }
}
