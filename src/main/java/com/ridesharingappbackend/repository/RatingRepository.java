package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByCompanyId(Integer companyId);

    @Query("SELECT r FROM Rating r WHERE r.company.id = :companyId AND r.driver.id = :driverId")
    List<Rating> findByCompanyIdAndDriverId(@Param("companyId") Integer companyId,
                                            @Param("driverId") Integer driverId);

    @Query("SELECT r FROM Rating r WHERE r.company.id = :companyId AND r.ride.id = :rideId")
    Optional<Rating> findByCompanyIdAndRideId(@Param("companyId") Integer companyId,
                                              @Param("rideId") Integer rideId);

    boolean existsByIdAndCompanyId(Integer id, Integer companyId);
}