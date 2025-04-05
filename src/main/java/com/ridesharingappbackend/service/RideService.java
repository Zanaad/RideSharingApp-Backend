package com.ridesharingappbackend.service;

import com.ridesharingappbackend.repository.RideRepository;
import org.springframework.stereotype.Service;

@Service
public class RideService {
    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public boolean deleteRide(Integer id) {
        if (rideRepository.existsById(id)) {
            rideRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
