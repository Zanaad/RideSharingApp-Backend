// DriverLicenseRepository.java
package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.DriverLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverLicenseRepository extends JpaRepository<DriverLicense, Integer> {
}
