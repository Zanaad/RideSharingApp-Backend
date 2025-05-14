package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.DriverDTO;
import com.ridesharingappbackend.mappers.DriverMapper;
import com.ridesharingappbackend.models.Driver;
import com.ridesharingappbackend.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;

    @InjectMocks
    private DriverService driverService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDrivers_ReturnsDTOList() {
        Driver driver = new Driver(); driver.setId(1);
        DriverDTO dto = new DriverDTO(); dto.setId(1);

        when(driverRepository.findAll()).thenReturn(List.of(driver));
        when(driverMapper.toDTO(driver)).thenReturn(dto);

        List<DriverDTO> result = driverService.getAllDrivers();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getDriverById_WhenExists_ReturnsDTO() {
        Driver driver = new Driver(); driver.setId(1);
        DriverDTO dto = new DriverDTO(); dto.setId(1);

        when(driverRepository.findById(1)).thenReturn(Optional.of(driver));
        when(driverMapper.toDTO(driver)).thenReturn(dto);

        Optional<DriverDTO> result = driverService.getDriverById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getDriverById_WhenNotExists_ReturnsEmptyOptional() {
        when(driverRepository.findById(99)).thenReturn(Optional.empty());

        Optional<DriverDTO> result = driverService.getDriverById(99);

        assertFalse(result.isPresent());
    }

    @Test
    void createDriver_SavesAndReturnsDTO() {
        DriverDTO dto = new DriverDTO(); dto.setId(1);
        Driver driver = new Driver(); driver.setId(1);

        when(driverMapper.toEntity(dto)).thenReturn(driver);
        when(driverRepository.save(driver)).thenReturn(driver);
        when(driverMapper.toDTO(driver)).thenReturn(dto);

        DriverDTO result = driverService.createDriver(dto);

        assertEquals(1, result.getId());
    }

    @Test
    void updateDriver_WhenExists_UpdatesAndReturnsDTO() {
        DriverDTO updatedDto = new DriverDTO(); updatedDto.setId(1);
        Driver existing = new Driver(); existing.setId(1);

        when(driverRepository.findById(1)).thenReturn(Optional.of(existing));
        doNothing().when(driverMapper).updateDriverFromDto(updatedDto, existing);
        when(driverRepository.save(existing)).thenReturn(existing);
        when(driverMapper.toDTO(existing)).thenReturn(updatedDto);

        DriverDTO result = driverService.updateDriver(1, updatedDto);

        assertEquals(1, result.getId());
        verify(driverMapper).updateDriverFromDto(updatedDto, existing);
    }

    @Test
    void updateDriver_WhenNotExists_ThrowsException() {
        DriverDTO dto = new DriverDTO();

        when(driverRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> driverService.updateDriver(99, dto));

        assertEquals("Driver not found", ex.getMessage());
    }

    @Test
    void deleteDriver_CallsRepositoryDelete() {
        doNothing().when(driverRepository).deleteById(1);

        driverService.deleteDriver(1);

        verify(driverRepository).deleteById(1);
    }
}
