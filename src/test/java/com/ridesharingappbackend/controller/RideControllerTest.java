package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.enums.RideStatus;
import com.ridesharingappbackend.service.RideService;
import com.ridesharingappbackend.service.RatingService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RideControllerTest {

    @InjectMocks
    private RideController rideController;

    @Mock
    private RideService rideService;

    @Mock
    private RatingService ratingService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private RideDTO createSampleRide() {
        RideDTO ride = new RideDTO();
        ride.setId(1);
        ride.setPassengerId(10);
        ride.setDriverId(20);
        ride.setCompanyId(1);
        ride.setStatus(RideStatus.REQUESTED);
        ride.setCreatedAt(LocalDateTime.now());
        return ride;
    }

    @Test
    void getAllRides_returnsList() {
        when(request.getAttribute("companyId")).thenReturn(1);
        RideDTO ride = createSampleRide();
        when(rideService.getRidesByCompany(1)).thenReturn(List.of(ride));

        List<RideDTO> rides = rideController.getAllRides(request);

        assertEquals(1, rides.size());
        assertEquals(ride, rides.get(0));
        verify(rideService).getRidesByCompany(1);
    }

    @Test
    void getRideById_found() {
        when(request.getAttribute("companyId")).thenReturn(1);
        RideDTO ride = createSampleRide();
        when(rideService.getRideById(1, 1)).thenReturn(Optional.of(ride));

        ResponseEntity<RideDTO> response = rideController.getRideById(1, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(ride, response.getBody());
    }

    @Test
    void getRideById_notFound() {
        when(request.getAttribute("companyId")).thenReturn(1);
        when(rideService.getRideById(999, 1)).thenReturn(Optional.empty());

        ResponseEntity<RideDTO> response = rideController.getRideById(999, request);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void getMyRides_returnsList() {
        when(request.getAttribute("userId")).thenReturn(10);
        when(request.getAttribute("companyId")).thenReturn(1);

        RideDTO ride = createSampleRide();
        when(rideService.getRidesByPassengerId(10, 1)).thenReturn(List.of(ride));

        List<RideDTO> rides = rideController.getMyRides(request);

        assertEquals(1, rides.size());
        assertEquals(ride, rides.get(0));
    }

    @Test
    void bookRide_success() {
        when(request.getAttribute("companyId")).thenReturn(1);
        when(request.getAttribute("userId")).thenReturn(10);

        RideDTO rideToCreate = createSampleRide();
        when(rideService.createRide(any(RideDTO.class), eq(1), eq(10))).thenReturn(rideToCreate);

        ResponseEntity<RideDTO> response = rideController.bookRide(rideToCreate, request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(rideToCreate, response.getBody());
    }

    @Test
    void bookRide_unauthorized() {
        when(request.getAttribute("companyId")).thenReturn(null);
        when(request.getAttribute("userId")).thenReturn(null);

        RideDTO rideToCreate = createSampleRide();
        ResponseEntity<RideDTO> response = rideController.bookRide(rideToCreate, request);

        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    void deleteRide_success() {
        when(request.getAttribute("companyId")).thenReturn(1);
        when(rideService.deleteRide(1, 1)).thenReturn(true);

        ResponseEntity<Void> response = rideController.deleteRide(1, request);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteRide_notFound() {
        when(request.getAttribute("companyId")).thenReturn(1);
        when(rideService.deleteRide(999, 1)).thenReturn(false);

        ResponseEntity<Void> response = rideController.deleteRide(999, request);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getMyRideHistory_authorized() {
        when(request.getAttribute("userId")).thenReturn(20);
        RideDTO ride = createSampleRide();
        when(rideService.getRidesByDriver(20)).thenReturn(List.of(ride));

        ResponseEntity<List<RideDTO>> response = rideController.getMyRideHistory(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(ride, response.getBody().get(0));
    }

    @Test
    void getMyRideHistory_unauthorized() {
        when(request.getAttribute("userId")).thenReturn(null);

        ResponseEntity<List<RideDTO>> response = rideController.getMyRideHistory(request);

        assertEquals(401, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void updateRideStatus_success() {
        when(request.getAttribute("userId")).thenReturn(10);
        RideDTO updateDto = new RideDTO();
        updateDto.setStatus(RideStatus.COMPLETED);

        when(rideService.updateRideStatus(1, 10, RideStatus.COMPLETED)).thenReturn(true);

        ResponseEntity<String> response = rideController.updateRideStatus(1, updateDto, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Ride status updated successfully", response.getBody());
    }

    @Test
    void updateRideStatus_badRequest() {
        when(request.getAttribute("userId")).thenReturn(10);
        RideDTO updateDto = new RideDTO(); // status null

        ResponseEntity<String> response = rideController.updateRideStatus(1, updateDto, request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid status", response.getBody());
    }

    @Test
    void updateRideStatus_couldNotUpdate() {
        when(request.getAttribute("userId")).thenReturn(10);
        RideDTO updateDto = new RideDTO();
        updateDto.setStatus(RideStatus.CANCELLED);

        when(rideService.updateRideStatus(1, 10, RideStatus.CANCELLED)).thenReturn(false);

        ResponseEntity<String> response = rideController.updateRideStatus(1, updateDto, request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Could not update ride status", response.getBody());
    }
}
