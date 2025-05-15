package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    Vehicle findByDriverId(Integer driverId);

    List<Vehicle> findByCompanyId(Integer companyId);

    Optional<Vehicle> findByIdAndCompanyId(Integer id, Integer companyId);

    boolean existsByIdAndCompanyId(Integer id, Integer companyId);
}