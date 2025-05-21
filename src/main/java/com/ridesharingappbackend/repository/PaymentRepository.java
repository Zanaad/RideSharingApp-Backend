package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.enums.PaymentStatus;
import com.ridesharingappbackend.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByCompanyId(Integer companyId);

    Optional<Payment> findByIdAndCompanyId(Integer id, Integer companyId);

    Payment findByCompanyIdAndRideId(Integer companyId, Integer rideId);

    List<Payment> findByCompanyIdAndUserId(Integer companyId, Integer userId);

    List<Payment> findByCompanyIdAndPaymentStatus(Integer companyId, PaymentStatus status);

    List<Payment> findByCompanyIdAndUserIdAndPaymentStatus(Integer companyId, Integer userId, PaymentStatus status);
}