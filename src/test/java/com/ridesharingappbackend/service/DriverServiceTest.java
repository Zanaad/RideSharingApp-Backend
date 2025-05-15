package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.DriverDTO;
import com.ridesharingappbackend.mappers.DriverMapper;
import com.ridesharingappbackend.models.Driver;
import com.ridesharingappbackend.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;

    @InjectMocks
    private DriverService driverService;

    private final Integer companyId = 10;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDriversByCompany_ReturnsDTOList() {
        Driver driver = new Driver();
        driver.setId(1);
        DriverDTO dto = new DriverDTO();
        dto.setId(1);

        when(driverRepository.findByCompanyId(companyId)).thenReturn(List.of(driver));
        when(driverMapper.toDTO(driver)).thenReturn(dto);

        List<DriverDTO> result = driverService.getAllDriversByCompany(companyId);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getDriverById_WhenExists_ReturnsDTO() {
        Driver driver = new Driver();
        driver.setId(1);
        DriverDTO dto = new DriverDTO();
        dto.setId(1);

        when(driverRepository.findByIdAndCompanyId(1, companyId)).thenReturn(Optional.of(driver));
        when(driverMapper.toDTO(driver)).thenReturn(dto);

        Optional<DriverDTO> result = driverService.getDriverById(1, companyId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getDriverById_WhenNotExists_ReturnsEmptyOptional() {
        when(driverRepository.findByIdAndCompanyId(99, companyId)).thenReturn(Optional.empty());

        Optional<DriverDTO> result = driverService.getDriverById(99, companyId);

        assertFalse(result.isPresent());
    }

    @Test
    void createDriver_SavesAndReturnsDTO() {
        DriverDTO dto = new DriverDTO();
        dto.setId(1);
        Driver driver = new Driver();
        driver.setId(1);

        when(driverMapper.toEntity(dto)).thenReturn(driver);
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);
        when(driverMapper.toDTO(driver)).thenReturn(dto);

        DriverDTO result = driverService.createDriver(dto, companyId);

        assertEquals(1, result.getId());
        assertEquals(companyId, driver.getCompany().getId());
    }

    @Test
    void updateDriver_WhenExists_UpdatesAndReturnsDTO() {
        DriverDTO updatedDto = new DriverDTO();
        updatedDto.setId(1);
        Driver existing = new Driver();
        existing.setId(1);

        when(driverRepository.findByIdAndCompanyId(1, companyId)).thenReturn(Optional.of(existing));
        doNothing().when(driverMapper).updateDriverFromDto(updatedDto, existing);
        when(driverRepository.save(existing)).thenReturn(existing);
        when(driverMapper.toDTO(existing)).thenReturn(updatedDto);

        DriverDTO result = driverService.updateDriver(1, updatedDto, companyId);

        assertEquals(1, result.getId());
        verify(driverMapper).updateDriverFromDto(updatedDto, existing);
    }

    @Test
    void updateDriver_WhenNotExists_ThrowsException() {
        DriverDTO dto = new DriverDTO();

        when(driverRepository.findByIdAndCompanyId(99, companyId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> driverService.updateDriver(99, dto, companyId));

        assertEquals("Driver not found or not part of this company", ex.getMessage());
    }

    @Test
    void deleteDriver_WhenExists_DeletesSuccessfully() {
        Driver driver = new Driver();
        driver.setId(1);
        when(driverRepository.findByIdAndCompanyId(1, companyId)).thenReturn(Optional.of(driver));
        doNothing().when(driverRepository).delete(driver);

        driverService.deleteDriver(1, companyId);

        verify(driverRepository).delete(driver);
    }

    @Test
    void deleteDriver_WhenNotExists_ThrowsException() {
        when(driverRepository.findByIdAndCompanyId(1, companyId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> driverService.deleteDriver(1, companyId));

        assertEquals("Driver not found or not part of this company", ex.getMessage());
    }
}
