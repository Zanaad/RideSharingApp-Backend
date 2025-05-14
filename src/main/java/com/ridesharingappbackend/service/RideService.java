package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.enums.RideStatus;
import com.ridesharingappbackend.mappers.RideMapper;
import com.ridesharingappbackend.models.Ride;
import com.ridesharingappbackend.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RideService {
    private final RideRepository rideRepository;
    private final RideMapper rideMapper;

    public RideService(RideRepository rideRepository, RideMapper rideMapper) {
        this.rideRepository = rideRepository;
        this.rideMapper = rideMapper;
    }

    public List<RideDTO> getAllRides() {
        return rideRepository.findAll().stream()
                .map(rideMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<RideDTO> getRideById(Integer id) {
        return rideRepository.findById(id)
                .map(rideMapper::toDTO);
    }

    public List<RideDTO> getRidesByPassengerId(Integer passengerId) {
        return rideRepository.findByPassengerId(passengerId).stream()
                .map(rideMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<RideDTO> getRidesByCompany(Integer companyId) {
        return rideRepository.findByCompanyId(companyId).stream()
                .map(rideMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<RideDTO> getRidesByCompanyAndStatus(Integer companyId, RideStatus status) {
        return rideRepository.findByCompanyIdAndStatus(companyId, status).stream()
                .map(rideMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<RideDTO> getRidesByCompanyAndDriver(Integer companyId, Integer driverId) {
        return rideRepository.findByCompanyAndDriver(companyId, driverId).stream()
                .map(rideMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<RideDTO> getRidesByCompanyAndPassenger(Integer companyId, Integer passengerId) {
        return rideRepository.findByCompanyAndPassenger(companyId, passengerId).stream()
                .map(rideMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RideDTO createRide(RideDTO rideDTO) {
        Ride ride = rideMapper.toEntity(rideDTO);
        return rideMapper.toDTO(rideRepository.save(ride));
    }

    public boolean deleteRide(Integer id) {
        if (rideRepository.existsById(id)) {
            rideRepository.deleteById(id);
            return true;
        }
        return false;
    }
}