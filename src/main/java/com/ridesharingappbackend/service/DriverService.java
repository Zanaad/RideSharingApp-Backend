package com.ridesharingappbackend.service;

import com.ridesharingappbackend.models.Driver;
import com.ridesharingappbackend.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {
    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Optional<Driver> getDriverById(Integer id) {
        return driverRepository.findById(id);
    }

    public Driver createDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public Driver updateDriver(Integer id, Driver updatedDriver) {
        return driverRepository.findById(id).map(driver -> {
            driver.setRating(updatedDriver.getRating());
            driver.setStatus(updatedDriver.getStatus());
            driver.setUser(updatedDriver.getUser());
            return driverRepository.save(driver);
        }).orElse(null);
    }

    public void deleteDriver(Integer id) {
        driverRepository.deleteById(id);
    }
}
