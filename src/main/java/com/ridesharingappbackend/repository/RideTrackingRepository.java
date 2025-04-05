package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.RideTracking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideTrackingRepository extends JpaRepository<RideTracking, Integer> {
}
