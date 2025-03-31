package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    List<Driver> findByAvailable(boolean available);
}
