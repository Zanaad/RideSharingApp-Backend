package com.ridesharingappbackend.service;

import com.ridesharingappbackend.models.Ride;
import com.ridesharingappbackend.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RideService {

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    public Optional<Ride> getRideById(Integer id) {
        return rideRepository.findById(id);
    }

    public List<Ride> getRidesByPassengerId(Integer passengerId) {
        return rideRepository.findByPassengerId(passengerId);
    }

    public Ride createRide(Ride ride) {
        return rideRepository.save(ride);
    }

    public boolean deleteRide(Integer id) {
        if (rideRepository.existsById(id)) {
            rideRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
