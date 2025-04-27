package com.ridesharingappbackend.service;

import com.ridesharingappbackend.models.Payment;
import com.ridesharingappbackend.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Integer id) {
        return paymentRepository.findById(id);
    }

    public Payment getPaymentByRideId(Integer rideId) {
        return paymentRepository.findByRideId(rideId);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public boolean deletePayment(Integer id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
