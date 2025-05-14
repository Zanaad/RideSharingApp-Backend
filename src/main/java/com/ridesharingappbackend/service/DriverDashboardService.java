package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.DriverDashboardDTO;
import com.ridesharingappbackend.models.Driver;
import com.ridesharingappbackend.models.DriverLicense;
import com.ridesharingappbackend.repository.DriverLicenseRepository;
import com.ridesharingappbackend.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DriverDashboardService {

    private final DriverRepository driverRepository;
    private final DriverLicenseRepository licenseRepository;

    public DriverDashboardService(DriverRepository driverRepository, DriverLicenseRepository licenseRepository) {
        this.driverRepository = driverRepository;
        this.licenseRepository = licenseRepository;
    }

    public DriverDashboardDTO getDashboardData(Integer driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        String fullName = driver.getUser().getFirstName() + " " + driver.getUser().getLastName();
        String companyName = driver.getCompany().getName();
        float rating = driver.getRating();
        String status = driver.getStatus().name();

        // Më e fundit (supozojmë se ka vetëm një për tani)
        List<DriverLicense> licenses = licenseRepository.findAll();
        DriverLicense currentLicense = licenses.stream()
                .filter(l -> l.getDriver().getId().equals(driverId))
                .max(Comparator.comparing(DriverLicense::getCreatedAt))
                .orElse(null);

        String licenseNumber = currentLicense != null ? currentLicense.getLicenseNumber() : "N/A";
        var expiry = currentLicense != null ? currentLicense.getExpirationDate() : null;

        // Po e vendosim statikisht totalTrips = 0 për tani, nëse s'ke tabelë të udhëtimeve
    return new DriverDashboardDTO(fullName, companyName, rating, status, 0, licenseNumber, expiry);
    }
}
