package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    Vehicle findByDriverId(Integer driverId);
}
