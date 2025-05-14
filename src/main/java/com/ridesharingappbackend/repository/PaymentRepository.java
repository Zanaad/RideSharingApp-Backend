package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.enums.PaymentStatus;
import com.ridesharingappbackend.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment findByRideId(Integer rideId);

    List<Payment> findByCompanyId(Integer companyId);

    @Query("SELECT p FROM Payment p WHERE p.company.id = :companyId AND p.ride.id = :rideId")
    Payment findByCompanyAndRideId(@Param("companyId") Integer companyId,
                                   @Param("rideId") Integer rideId);

    @Query("SELECT p FROM Payment p WHERE p.company.id = :companyId AND p.user.id = :userId")
    List<Payment> findByCompanyAndUserId(@Param("companyId") Integer companyId,
                                         @Param("userId") Integer userId);

    @Query("SELECT p FROM Payment p WHERE p.company.id = :companyId AND p.paymentStatus = :status")
    List<Payment> findByCompanyIdAndPaymentStatus(@Param("companyId") Integer companyId,
                                                  @Param("status") PaymentStatus status);

    boolean existsByIdAndCompanyId(Integer id, Integer companyId);
}