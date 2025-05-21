package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.enums.DriverStatus;
import com.ridesharingappbackend.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Optional<Driver> findByUserId(Integer userId);

    List<Driver> findByCompanyId(Integer companyId);

    Optional<Driver> findByIdAndCompanyId(Integer id, Integer companyId);

    List<Driver> findByCompanyIdAndStatus(Integer companyId, DriverStatus status);
}