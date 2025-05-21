package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.enums.DriverStatus;
import com.ridesharingappbackend.enums.RideStatus;
import com.ridesharingappbackend.exceptions.NoAvailableDriversException;
import com.ridesharingappbackend.mappers.RideMapper;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Driver;
import com.ridesharingappbackend.models.Ride;
import com.ridesharingappbackend.models.User;
import com.ridesharingappbackend.repository.CompanyRepository;
import com.ridesharingappbackend.repository.DriverRepository;
import com.ridesharingappbackend.repository.RideRepository;
import com.ridesharingappbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RideService {
    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final RideMapper rideMapper;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    private final PaymentService paymentService;

    public RideService(RideRepository rideRepository, DriverRepository driverRepository, RideMapper rideMapper, CompanyRepository companyRepository, UserRepository userRepository, PaymentService paymentService) {
        this.rideRepository = rideRepository;
        this.driverRepository = driverRepository;
        this.rideMapper = rideMapper;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;

        this.paymentService = paymentService;
    }

    public List<RideDTO> getRidesByCompany(Integer companyId) {
        return rideRepository.findByCompanyId(companyId).stream().map(rideMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<RideDTO> getRideById(Integer id, Integer companyId) {
        return rideRepository.findById(id).filter(r -> r.getCompany().getId().equals(companyId)).map(rideMapper::toDTO);
    }

    public List<RideDTO> getRidesByPassengerId(Integer passengerId, Integer companyId) {
        return rideRepository.findByCompanyAndPassenger(companyId, passengerId).stream().map(rideMapper::toDTO).collect(Collectors.toList());
    }

    public List<RideDTO> getRidesByCompanyAndStatus(Integer companyId, RideStatus status) {
        return rideRepository.findByCompanyIdAndStatus(companyId, status).stream().map(rideMapper::toDTO).collect(Collectors.toList());
    }

    public List<RideDTO> getRidesByCompanyAndDriver(Integer companyId, Integer driverId) {
        return rideRepository.findByCompanyAndDriver(companyId, driverId).stream().map(rideMapper::toDTO).collect(Collectors.toList());
    }

    public List<RideDTO> getRidesByCompanyAndPassenger(Integer companyId, Integer passengerId) {
        return rideRepository.findByCompanyAndPassenger(companyId, passengerId).stream().map(rideMapper::toDTO).collect(Collectors.toList());
    }

    public RideDTO createRide(RideDTO rideDTO, Integer companyId, Integer passengerId) {
        Ride ride = rideMapper.toEntity(rideDTO);

        Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));

        User passenger = userRepository.findById(passengerId).orElseThrow(() -> new RuntimeException("User not found"));

        List<Driver> availableDrivers = driverRepository.findByCompanyIdAndStatus(companyId, DriverStatus.AVAILABLE);
        if (availableDrivers.isEmpty()) {
            throw new NoAvailableDriversException("No available drivers at the moment");
        }

        Driver assignedDriver = availableDrivers.get(0);
        assignedDriver.setStatus(DriverStatus.BUSY);
        driverRepository.save(assignedDriver);

        ride.setCompany(company);
        ride.setPassenger(passenger);
        ride.setDriver(assignedDriver);
        ride.setStatus(RideStatus.ACCEPTED);

        Ride savedRide = rideRepository.save(ride);

        return rideMapper.toDTO(savedRide);
    }

    public boolean deleteRide(Integer id, Integer companyId) {
        return rideRepository.findById(id).filter(r -> r.getCompany().getId().equals(companyId)).map(r -> {
            rideRepository.delete(r);
            return true;
        }).orElse(false);
    }

    public List<RideDTO> getRidesByDriver(Integer userId) {
        Optional<Driver> driverOpt = driverRepository.findByUserId(userId);

        if (driverOpt.isEmpty()) {
            return new ArrayList<>(); // ose hidh një exception nëse duhet
        }

        Integer driverId = driverOpt.get().getId();
        List<Ride> rides = rideRepository.findByDriver_Id(driverId);
        return rides.stream().map(rideMapper::toDTO).collect(Collectors.toList());
    }
    public boolean updateRideStatus(Integer rideId, Integer userId, RideStatus newStatus) {
        return driverRepository.findByUserId(userId)
                .flatMap(driver -> rideRepository.findByIdAndDriver_Id(rideId, driver.getId()))
                .map(ride -> {
                    ride.setStatus(newStatus);
                    rideRepository.save(ride);
                    return true;
                })
                .orElse(false);
    }

}


