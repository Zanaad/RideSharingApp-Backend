package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.models.Payment;
import com.ridesharingappbackend.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public Optional<Payment> getPaymentById(@PathVariable Integer id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping("/ride/{rideId}")
    public Payment getPaymentByRideId(@PathVariable Integer rideId) {
        return paymentService.getPaymentByRideId(rideId);
    }

    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentService.createPayment(payment);
    }

    @DeleteMapping("/{id}")
    public boolean deletePayment(@PathVariable Integer id) {
        return paymentService.deletePayment(id);
    }
}
