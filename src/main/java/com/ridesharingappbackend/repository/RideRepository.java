package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.enums.RideStatus;
import com.ridesharingappbackend.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Integer> {
    List<Ride> findByPassengerId(Integer passengerId);

    List<Ride> findByDriverId(Integer driverId);

    List<Ride> findByStatus(RideStatus status);
}
