package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.enums.DriverStatus;
import com.ridesharingappbackend.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Driver findByUserId(Integer userId);
    List<Driver> findByStatus(DriverStatus status);
    List<Driver> findByCompanyId(Integer companyId);
}