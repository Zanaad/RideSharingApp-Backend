package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.mappers.RideMapper;
import com.ridesharingappbackend.models.Ride;
import com.ridesharingappbackend.repository.RideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RideMapper rideMapper;

    @InjectMocks
    private RideService rideService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRides_ReturnsListOfDTOs() {
        Ride ride = new Ride(); ride.setId(1);
        RideDTO dto = new RideDTO(); dto.setId(1);

        when(rideRepository.findAll()).thenReturn(List.of(ride));
        when(rideMapper.toDTO(ride)).thenReturn(dto);

        List<RideDTO> result = rideService.getAllRides();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getRideById_WhenExists_ReturnsDTO() {
        Ride ride = new Ride(); ride.setId(1);
        RideDTO dto = new RideDTO(); dto.setId(1);

        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));
        when(rideMapper.toDTO(ride)).thenReturn(dto);

        Optional<RideDTO> result = rideService.getRideById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getRideById_WhenNotExists_ReturnsEmpty() {
        when(rideRepository.findById(100)).thenReturn(Optional.empty());

        Optional<RideDTO> result = rideService.getRideById(100);

        assertFalse(result.isPresent());
    }

    @Test
    void getRidesByPassengerId_ReturnsDTOList() {
        Ride ride = new Ride(); ride.setId(1);
        RideDTO dto = new RideDTO(); dto.setId(1);

        when(rideRepository.findByPassengerId(42)).thenReturn(List.of(ride));
        when(rideMapper.toDTO(ride)).thenReturn(dto);

        List<RideDTO> result = rideService.getRidesByPassengerId(42);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void createRide_SavesEntity_ReturnsDTO() {
        RideDTO dto = new RideDTO(); dto.setId(1);
        Ride ride = new Ride(); ride.setId(1);

        when(rideMapper.toEntity(dto)).thenReturn(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(rideMapper.toDTO(ride)).thenReturn(dto);

        RideDTO result = rideService.createRide(dto);

        assertEquals(1, result.getId());
    }

    @Test
    void deleteRide_WhenExists_ReturnsTrue() {
        when(rideRepository.existsById(1)).thenReturn(true);
        doNothing().when(rideRepository).deleteById(1);

        boolean result = rideService.deleteRide(1);

        assertTrue(result);
        verify(rideRepository).deleteById(1);
    }

    @Test
    void deleteRide_WhenNotExists_ReturnsFalse() {
        when(rideRepository.existsById(2)).thenReturn(false);

        boolean result = rideService.deleteRide(2);

        assertFalse(result);
        verify(rideRepository, never()).deleteById(any());
    }
}
