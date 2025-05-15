package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.VehicleDTO;
import com.ridesharingappbackend.mappers.VehicleMapper;
import com.ridesharingappbackend.models.Vehicle;
import com.ridesharingappbackend.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllVehicles_WithCompanyId_ReturnsFilteredList() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        VehicleDTO dto = new VehicleDTO();
        dto.setId(1);

        when(vehicleRepository.findByCompanyId(10)).thenReturn(List.of(vehicle));
        when(vehicleMapper.toDTO(vehicle)).thenReturn(dto);

        List<VehicleDTO> result = vehicleService.getAllVehicles(10);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getAllVehicles_WithoutCompanyId_ReturnsAll() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(2);
        VehicleDTO dto = new VehicleDTO();
        dto.setId(2);

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));
        when(vehicleMapper.toDTO(vehicle)).thenReturn(dto);

        List<VehicleDTO> result = vehicleService.getAllVehicles(null);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getId());
    }

    @Test
    void getVehicleById_WhenExists_ReturnsDTO() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        VehicleDTO dto = new VehicleDTO();
        dto.setId(1);

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.toDTO(vehicle)).thenReturn(dto);

        Optional<VehicleDTO> result = vehicleService.getVehicleById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getVehicleById_WhenNotExists_ReturnsEmptyOptional() {
        when(vehicleRepository.findById(99)).thenReturn(Optional.empty());

        Optional<VehicleDTO> result = vehicleService.getVehicleById(99);

        assertFalse(result.isPresent());
    }

    @Test
    void getVehicleByDriverId_ReturnsDTO() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        VehicleDTO dto = new VehicleDTO();
        dto.setId(1);

        when(vehicleRepository.findByDriverId(10)).thenReturn(vehicle);
        when(vehicleMapper.toDTO(vehicle)).thenReturn(dto);

        VehicleDTO result = vehicleService.getVehicleByDriverId(10);

        assertEquals(1, result.getId());
    }

    @Test
    void createVehicle_ReturnsSavedDTO() {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(1);
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);

        when(vehicleMapper.toEntity(dto)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toDTO(vehicle)).thenReturn(dto);

        VehicleDTO result = vehicleService.createVehicle(dto);

        assertEquals(1, result.getId());
    }

    @Test
    void updateVehicle_WhenExists_UpdatesAndReturnsDTO() {
        Vehicle existing = new Vehicle();
        existing.setId(1);
        VehicleDTO dto = new VehicleDTO();
        dto.setId(1);

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(existing));
        doNothing().when(vehicleMapper).updateVehicleFromDto(dto, existing);
        when(vehicleRepository.save(existing)).thenReturn(existing);
        when(vehicleMapper.toDTO(existing)).thenReturn(dto);

        VehicleDTO result = vehicleService.updateVehicle(1, dto);

        assertEquals(1, result.getId());
    }

    @Test
    void updateVehicle_WhenNotExists_ThrowsException() {
        VehicleDTO dto = new VehicleDTO();

        when(vehicleRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> vehicleService.updateVehicle(999, dto));

        assertEquals("Vehicle not found", ex.getMessage());
    }

    @Test
    void deleteVehicle_WhenExists_ReturnsTrue() {
        when(vehicleRepository.existsById(1)).thenReturn(true);
        doNothing().when(vehicleRepository).deleteById(1);

        boolean result = vehicleService.deleteVehicle(1);

        assertTrue(result);
        verify(vehicleRepository).deleteById(1);
    }

    @Test
    void deleteVehicle_WhenNotExists_ReturnsFalse() {
        when(vehicleRepository.existsById(2)).thenReturn(false);

        boolean result = vehicleService.deleteVehicle(2);

        assertFalse(result);
        verify(vehicleRepository, never()).deleteById(any());
    }
}
