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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class RideService {
    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;


    public RideService(RideRepository rideRepository,
                       CompanyRepository companyRepository,
                       UserRepository userRepository,
                       RideMapper rideMapper) {
        this.rideRepository = rideRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.rideMapper = rideMapper;
    }


    public List<RideDTO> getRidesByCompany(Integer companyId) {
        return rideRepository.findByCompanyId(companyId).stream()
                .map(rideMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<RideDTO> getRideById(Integer id, Integer companyId) {
        return rideRepository.findById(id)
                .filter(r -> r.getCompany().getId().equals(companyId))
                .map(rideMapper::toDTO);
    }

    public List<RideDTO> getRidesByPassengerId(Integer passengerId, Integer companyId) {
        return rideRepository.findByCompanyAndPassenger(companyId, passengerId).stream()
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

    public RideDTO createRide(RideDTO rideDTO, Integer companyId, Integer passengerId) {
        Ride ride = rideMapper.toEntity(rideDTO);

        // Fetch Company entity
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Fetch User (passenger) entity
        User passenger = userRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set associations
        ride.setCompany(company);
        ride.setPassenger(passenger);

        Ride savedRide = rideRepository.save(ride);
        return rideMapper.toDTO(savedRide);
    }

    public boolean deleteRide(Integer id, Integer companyId) {
        return rideRepository.findById(id)
                .filter(r -> r.getCompany().getId().equals(companyId))
                .map(r -> {
                    rideRepository.delete(r);
                    return true;
                })
                .orElse(false);
    }

}
