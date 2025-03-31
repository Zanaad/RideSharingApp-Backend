package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Integer> {
}
