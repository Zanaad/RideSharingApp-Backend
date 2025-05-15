package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RideTrackingDTO;
import com.ridesharingappbackend.mappers.RideTrackingMapper;
import com.ridesharingappbackend.models.RideTracking;
import com.ridesharingappbackend.repository.RideTrackingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RideTrackingServiceTest {

    @Mock
    private RideTrackingRepository rideTrackingRepository;

    @Mock
    private RideTrackingMapper mapper;

    @InjectMocks
    private RideTrackingService rideTrackingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRideTrackings_ReturnsDTOList() {
        RideTracking rideTracking = new RideTracking();
        rideTracking.setId(1);
        RideTrackingDTO dto = new RideTrackingDTO();
        dto.setId(1);

        when(rideTrackingRepository.findAll()).thenReturn(List.of(rideTracking));
        when(mapper.toDTO(rideTracking)).thenReturn(dto);

        List<RideTrackingDTO> result = rideTrackingService.getAllRideTrackings();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        verify(rideTrackingRepository).findAll();
        verify(mapper).toDTO(rideTracking);
    }

    @Test
    void getRideTrackingById_WhenExists_ReturnsDTO() {
        RideTracking rideTracking = new RideTracking();
        rideTracking.setId(1);
        RideTrackingDTO dto = new RideTrackingDTO();
        dto.setId(1);

        when(rideTrackingRepository.findById(1)).thenReturn(Optional.of(rideTracking));
        when(mapper.toDTO(rideTracking)).thenReturn(dto);

        Optional<RideTrackingDTO> result = rideTrackingService.getRideTrackingById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(rideTrackingRepository).findById(1);
        verify(mapper).toDTO(rideTracking);
    }

    @Test
    void getRideTrackingById_WhenNotExists_ReturnsEmptyOptional() {
        when(rideTrackingRepository.findById(99)).thenReturn(Optional.empty());

        Optional<RideTrackingDTO> result = rideTrackingService.getRideTrackingById(99);

        assertFalse(result.isPresent());
        verify(rideTrackingRepository).findById(99);
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void createRideTracking_SavesEntityAndReturnsDTO() {
        RideTrackingDTO dto = new RideTrackingDTO();
        dto.setId(1);
        RideTracking entity = new RideTracking();
        entity.setId(1);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(rideTrackingRepository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        RideTrackingDTO result = rideTrackingService.createRideTracking(dto);

        assertEquals(1, result.getId());
        verify(mapper).toEntity(dto);
        verify(rideTrackingRepository).save(entity);
        verify(mapper).toDTO(entity);
    }

    @Test
    void deleteRideTracking_WhenExists_ReturnsTrue() {
        when(rideTrackingRepository.existsById(1)).thenReturn(true);
        doNothing().when(rideTrackingRepository).deleteById(1);

        boolean result = rideTrackingService.deleteRideTracking(1);

        assertTrue(result);
        verify(rideTrackingRepository).existsById(1);
        verify(rideTrackingRepository).deleteById(1);
    }

    @Test
    void deleteRideTracking_WhenNotExists_ReturnsFalse() {
        when(rideTrackingRepository.existsById(2)).thenReturn(false);

        boolean result = rideTrackingService.deleteRideTracking(2);

        assertFalse(result);
        verify(rideTrackingRepository).existsById(2);
        verify(rideTrackingRepository, never()).deleteById(any());
    }
}
