package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.DriverDTO;
import com.ridesharingappbackend.dto.UserDTO;
import com.ridesharingappbackend.enums.DriverStatus;
import com.ridesharingappbackend.service.DriverService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriverControllerTest {

    @InjectMocks
    private DriverController driverController;

    @Mock
    private DriverService driverService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private DriverDTO createSampleDriverDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setEmail("driver@example.com");

        DriverDTO driver = new DriverDTO();
        driver.setId(1);
        driver.setUser(userDTO);
        driver.setRating(4.5f);
        driver.setStatus(DriverStatus.AVAILABLE);
        driver.setCompanyId(1);

        return driver;
    }

    @Test
    void getAllDrivers_success() {
        DriverDTO driver1 = createSampleDriverDTO();
        when(request.getAttribute("companyId")).thenReturn(1);
        when(driverService.getAllDriversByCompany(1)).thenReturn(List.of(driver1));

        ResponseEntity<List<DriverDTO>> response = driverController.getAllDrivers(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(driver1, response.getBody().get(0));
    }

    @Test
    void getDriverById_found() {
        DriverDTO driver = createSampleDriverDTO();
        when(request.getAttribute("companyId")).thenReturn(1);
        when(driverService.getDriverById(1, 1)).thenReturn(Optional.of(driver));

        ResponseEntity<DriverDTO> response = driverController.getDriverById(1, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(driver, response.getBody());
    }

    @Test
    void getDriverById_notFound() {
        when(request.getAttribute("companyId")).thenReturn(1);
        when(driverService.getDriverById(999, 1)).thenReturn(Optional.empty());

        ResponseEntity<DriverDTO> response = driverController.getDriverById(999, request);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void createDriver_success() {
        DriverDTO input = createSampleDriverDTO();
        when(request.getAttribute("companyId")).thenReturn(1);
        when(driverService.createDriver(input, 1)).thenReturn(input);

        ResponseEntity<DriverDTO> response = driverController.createDriver(input, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(input, response.getBody());
    }

    @Test
    void updateDriver_success() {
        DriverDTO updated = createSampleDriverDTO();
        updated.setRating(4.9f);

        when(request.getAttribute("companyId")).thenReturn(1);
        when(driverService.updateDriver(1, updated, 1)).thenReturn(updated);

        ResponseEntity<DriverDTO> response = driverController.updateDriver(1, updated, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updated, response.getBody());
    }

    @Test
    void deleteDriver_success() {
        when(request.getAttribute("companyId")).thenReturn(1);
        doNothing().when(driverService).deleteDriver(1, 1);

        ResponseEntity<Void> response = driverController.deleteDriver(1, request);

        assertEquals(204, response.getStatusCodeValue());
    }
}
