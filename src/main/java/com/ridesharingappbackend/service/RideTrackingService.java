package com.ridesharingappbackend.service;

import com.ridesharingappbackend.models.RideTracking;
import com.ridesharingappbackend.repository.RideTrackingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RideTrackingService {

    private final RideTrackingRepository rideTrackingRepository;

    public RideTrackingService(RideTrackingRepository rideTrackingRepository) {
        this.rideTrackingRepository = rideTrackingRepository;
    }

    public List<RideTracking> getAllRideTrackings() {
        return rideTrackingRepository.findAll();
    }

    public Optional<RideTracking> getRideTrackingById(Integer id) {
        return rideTrackingRepository.findById(id);
    }

    public RideTracking createRideTracking(RideTracking rideTracking) {
        return rideTrackingRepository.save(rideTracking);
    }

    public boolean deleteRideTracking(Integer id) {
        if (rideTrackingRepository.existsById(id)) {
            rideTrackingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
