package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.VehicleDTO;
import com.ridesharingappbackend.mappers.VehicleMapper;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Vehicle;
import com.ridesharingappbackend.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

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
    void getAllVehiclesByCompany_ReturnsList() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        VehicleDTO dto = new VehicleDTO();
        dto.setId(1);

        when(vehicleRepository.findByCompanyId(10)).thenReturn(List.of(vehicle));
        when(vehicleMapper.toDTO(vehicle)).thenReturn(dto);

        List<VehicleDTO> result = vehicleService.getAllVehiclesByCompany(10);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getVehicleByIdAndCompanyId_WhenExists_ReturnsDTO() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        Company company = new Company();
        company.setId(10);
        vehicle.setCompany(company);

        VehicleDTO dto = new VehicleDTO();
        dto.setId(1);

        when(vehicleRepository.findByIdAndCompanyId(1, 10)).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.toDTO(vehicle)).thenReturn(dto);

        Optional<VehicleDTO> result = vehicleService.getVehicleByIdAndCompanyId(1, 10);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getVehicleByIdAndCompanyId_WhenNotExists_ReturnsEmpty() {
        when(vehicleRepository.findByIdAndCompanyId(1, 10)).thenReturn(Optional.empty());

        Optional<VehicleDTO> result = vehicleService.getVehicleByIdAndCompanyId(1, 10);

        assertFalse(result.isPresent());
    }

    @Test
    void getVehicleByDriverIdAndCompanyId_WhenBelongsToCompany_ReturnsDTO() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        Company company = new Company();
        company.setId(10);
        vehicle.setCompany(company);

        VehicleDTO dto = new VehicleDTO();
        dto.setId(1);

        when(vehicleRepository.findByDriverId(5)).thenReturn(vehicle);
        when(vehicleMapper.toDTO(vehicle)).thenReturn(dto);

        Optional<VehicleDTO> result = vehicleService.getVehicleByDriverIdAndCompanyId(5, 10);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getVehicleByDriverIdAndCompanyId_WhenNotBelongsToCompany_ReturnsEmpty() {
        Vehicle vehicle = new Vehicle();
        Company company = new Company();
        company.setId(99); // different company
        vehicle.setCompany(company);

        when(vehicleRepository.findByDriverId(5)).thenReturn(vehicle);

        Optional<VehicleDTO> result = vehicleService.getVehicleByDriverIdAndCompanyId(5, 10);

        assertFalse(result.isPresent());
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
    void updateVehicle_WhenVehicleExists_ReturnsUpdatedDTO() {
        Vehicle existing = new Vehicle();
        existing.setId(1);
        Company company = new Company();
        company.setId(10);
        existing.setCompany(company);

        VehicleDTO dto = new VehicleDTO();
        dto.setId(1);

        when(vehicleRepository.findByIdAndCompanyId(1, 10)).thenReturn(Optional.of(existing));
        doNothing().when(vehicleMapper).updateVehicleFromDto(dto, existing);
        when(vehicleRepository.save(existing)).thenReturn(existing);
        when(vehicleMapper.toDTO(existing)).thenReturn(dto);

        VehicleDTO result = vehicleService.updateVehicle(1, 10, dto);

        assertEquals(1, result.getId());
    }

    @Test
    void updateVehicle_WhenVehicleNotFound_ThrowsException() {
        VehicleDTO dto = new VehicleDTO();

        when(vehicleRepository.findByIdAndCompanyId(999, 10)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> vehicleService.updateVehicle(999, 10, dto));

        assertEquals("Vehicle not found or does not belong to the company", ex.getMessage());
    }

    @Test
    void deleteVehicle_WhenVehicleExists_ReturnsTrue() {
        Vehicle vehicle = new Vehicle();
        Company company = new Company();
        company.setId(10);
        vehicle.setCompany(company);

        when(vehicleRepository.findByIdAndCompanyId(1, 10)).thenReturn(Optional.of(vehicle));
        doNothing().when(vehicleRepository).delete(vehicle);

        boolean result = vehicleService.deleteVehicle(1, 10);

        assertTrue(result);
        verify(vehicleRepository).delete(vehicle);
    }

    @Test
    void deleteVehicle_WhenVehicleNotFound_ReturnsFalse() {
        when(vehicleRepository.findByIdAndCompanyId(2, 10)).thenReturn(Optional.empty());

        boolean result = vehicleService.deleteVehicle(2, 10);

        assertFalse(result);
        verify(vehicleRepository, never()).delete(any());
    }
}
