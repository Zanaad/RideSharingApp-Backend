package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.enums.RideStatus;
import com.ridesharingappbackend.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Integer> {


    Optional<Ride> findTopByDriver_IdAndStatusInOrderByIdDesc(Integer driverId, List<RideStatus> statuses);
    Optional<Ride> findByIdAndDriver_Id(Integer rideId, Integer driverId);
    List<Ride> findByPassengerId(Integer passengerId);


List<Ride> findByDriver_Id(int driverId);
    List<Ride> findByStatus(RideStatus status);

    List<Ride> findByCompanyId(Integer companyId);

    List<Ride> findByCompanyIdAndStatus(Integer companyId, RideStatus status);

    @Query("SELECT r FROM Ride r WHERE r.company.id = :companyId AND r.driver.id = :driverId")
    List<Ride> findByCompanyAndDriver(@Param("companyId") Integer companyId,
                                      @Param("driverId") Integer driverId);

    @Query("SELECT r FROM Ride r WHERE r.company.id = :companyId AND r.passenger.id = :passengerId")
    List<Ride> findByCompanyAndPassenger(@Param("companyId") Integer companyId,
                                         @Param("passengerId") Integer passengerId);

    @Query("SELECT COUNT(r) FROM Ride r WHERE r.driver.id= :driverId AND r.status = 'COMPLETED' ")
    Long countCompletedRides(@Param("driverId") Long driverId);

    @Query("SELECT SUM(r.fare) FROM Ride r WHERE r.driver.id = :driverId AND r.status = 'COMPLETED'")
    Double sumTotalEarnings(@Param("driverId") Long driverId);

    @Query("SELECT r FROM Ride r WHERE r.driver.id = :driverId AND r.status IN ('ONGOING', 'ACCEPTED')")
    Optional<Ride> findCurrentRide(@Param("driverId") Long driverId);

}