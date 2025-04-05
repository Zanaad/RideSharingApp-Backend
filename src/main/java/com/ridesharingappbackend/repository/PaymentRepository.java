package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment findByRideId(Integer rideId);
}
