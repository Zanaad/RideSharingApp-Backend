package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.enums.RideStatus;
import com.ridesharingappbackend.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Integer> {
    List<Ride> findByPassengerId(Integer passengerId);

    List<Ride> findByDriverId(Integer driverId);

    List<Ride> findByStatus(RideStatus status);

    List<Ride> findByCompanyId(Integer companyId);

    List<Ride> findByCompanyIdAndStatus(Integer companyId, RideStatus status);

    @Query("SELECT r FROM Ride r WHERE r.company.id = :companyId AND r.driver.id = :driverId")
    List<Ride> findByCompanyAndDriver(@Param("companyId") Integer companyId,
                                      @Param("driverId") Integer driverId);

    @Query("SELECT r FROM Ride r WHERE r.company.id = :companyId AND r.passenger.id = :passengerId")
    List<Ride> findByCompanyAndPassenger(@Param("companyId") Integer companyId,
                                         @Param("passengerId") Integer passengerId);
}