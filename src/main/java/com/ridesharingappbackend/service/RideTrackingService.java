package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RideTrackingDTO;
import com.ridesharingappbackend.mappers.RideTrackingMapper;
import com.ridesharingappbackend.models.RideTracking;
import com.ridesharingappbackend.repository.RideTrackingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RideTrackingService {

    private final RideTrackingRepository rideTrackingRepository;
    private final RideTrackingMapper mapper;

    public RideTrackingService(RideTrackingRepository rideTrackingRepository, RideTrackingMapper mapper) {
        this.rideTrackingRepository = rideTrackingRepository;
        this.mapper = mapper;
    }

    public List<RideTrackingDTO> getAllRideTrackings() {
        return rideTrackingRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<RideTrackingDTO> getRideTrackingById(Integer id) {
        return rideTrackingRepository.findById(id)
                .map(mapper::toDTO);
    }

    public RideTrackingDTO createRideTracking(RideTrackingDTO dto) {
        RideTracking tracking = mapper.toEntity(dto);
        return mapper.toDTO(rideTrackingRepository.save(tracking));
    }

    public boolean deleteRideTracking(Integer id) {
        if (rideTrackingRepository.existsById(id)) {
            rideTrackingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
