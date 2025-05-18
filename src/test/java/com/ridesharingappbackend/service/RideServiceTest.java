package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.enums.RideStatus;
import com.ridesharingappbackend.mappers.RideMapper;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Ride;
import com.ridesharingappbackend.models.User;
import com.ridesharingappbackend.repository.CompanyRepository;
import com.ridesharingappbackend.repository.RideRepository;
import com.ridesharingappbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RideMapper rideMapper;

    @InjectMocks
    private RideService rideService;

    private final Integer companyId = 5;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRidesByCompany_ReturnsListOfDTOs() {
        Ride ride = new Ride();
        ride.setId(1);
        RideDTO dto = new RideDTO();
        dto.setId(1);

        when(rideRepository.findByCompanyId(companyId)).thenReturn(List.of(ride));
        when(rideMapper.toDTO(ride)).thenReturn(dto);

        List<RideDTO> result = rideService.getRidesByCompany(companyId);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getRideById_WhenCompanyMatches_ReturnsDTO() {
        Company company = new Company();
        company.setId(companyId);
        Ride ride = new Ride();
        ride.setId(1);
        ride.setCompany(company);
        RideDTO dto = new RideDTO();
        dto.setId(1);

        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));
        when(rideMapper.toDTO(ride)).thenReturn(dto);

        Optional<RideDTO> result = rideService.getRideById(1, companyId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getRideById_WhenCompanyMismatch_ReturnsEmpty() {
        Company company = new Company();
        company.setId(999);
        Ride ride = new Ride();
        ride.setId(1);
        ride.setCompany(company);

        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));

        Optional<RideDTO> result = rideService.getRideById(1, companyId);

        assertTrue(result.isEmpty());
    }

    @Test
    void getRidesByPassengerId_ReturnsListOfDTOs() {
        Ride ride = new Ride();
        ride.setId(1);
        RideDTO dto = new RideDTO();
        dto.setId(1);

        when(rideRepository.findByCompanyAndPassenger(companyId, 101)).thenReturn(List.of(ride));
        when(rideMapper.toDTO(ride)).thenReturn(dto);

        List<RideDTO> result = rideService.getRidesByPassengerId(101, companyId);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getRidesByCompanyAndStatus_ReturnsListOfDTOs() {
        Ride ride = new Ride();
        ride.setId(2);
        RideDTO dto = new RideDTO();
        dto.setId(2);

        when(rideRepository.findByCompanyIdAndStatus(companyId, RideStatus.REQUESTED)).thenReturn(List.of(ride));
        when(rideMapper.toDTO(ride)).thenReturn(dto);

        List<RideDTO> result = rideService.getRidesByCompanyAndStatus(companyId, RideStatus.REQUESTED);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getId());
    }


    @Test
    void getRidesByCompanyAndDriver_ReturnsListOfDTOs() {
        Ride ride = new Ride();
        ride.setId(3);
        RideDTO dto = new RideDTO();
        dto.setId(3);

        when(rideRepository.findByCompanyAndDriver(companyId, 200)).thenReturn(List.of(ride));
        when(rideMapper.toDTO(ride)).thenReturn(dto);

        List<RideDTO> result = rideService.getRidesByCompanyAndDriver(companyId, 200);

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getId());
    }

    @Test
    void createRide_AssignsCompanyAndSaves_ReturnsDTO() {
        Integer passengerId = 10;

        RideDTO dto = new RideDTO();
        dto.setId(1);
        Ride ride = new Ride();
        ride.setId(1);

        Company company = new Company();
        company.setId(companyId);

        User passenger = new User();
        passenger.setId(passengerId);

        when(rideMapper.toEntity(dto)).thenReturn(ride);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(userRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);
        when(rideMapper.toDTO(ride)).thenReturn(dto);

        RideDTO result = rideService.createRide(dto, companyId, passengerId);

        assertEquals(1, result.getId());
        verify(rideRepository).save(ride);
        assertEquals(company, ride.getCompany());
        assertEquals(passenger, ride.getPassenger());
    }



    @Test
    void deleteRide_WhenCompanyMatches_DeletesAndReturnsTrue() {
        Company company = new Company();
        company.setId(companyId);
        Ride ride = new Ride();
        ride.setId(1);
        ride.setCompany(company);

        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));

        boolean result = rideService.deleteRide(1, companyId);

        assertTrue(result);
        verify(rideRepository).delete(ride);
    }

    @Test
    void deleteRide_WhenCompanyDoesNotMatch_ReturnsFalse() {
        Company company = new Company();
        company.setId(999);
        Ride ride = new Ride();
        ride.setId(1);
        ride.setCompany(company);

        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));

        boolean result = rideService.deleteRide(1, companyId);

        assertFalse(result);
        verify(rideRepository, never()).delete(any());
    }

    @Test
    void deleteRide_WhenNotExists_ReturnsFalse() {
        when(rideRepository.findById(1)).thenReturn(Optional.empty());

        boolean result = rideService.deleteRide(1, companyId);

        assertFalse(result);
    }
}