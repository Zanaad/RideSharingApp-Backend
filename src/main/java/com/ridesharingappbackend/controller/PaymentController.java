package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.PaymentDTO;
import com.ridesharingappbackend.service.PaymentService;
import org.springframework.http.ResponseEntity;
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
    public List<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Integer id) {
        Optional<PaymentDTO> dto = paymentService.getPaymentById(id);
        return dto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ride/{rideId}")
    public ResponseEntity<PaymentDTO> getPaymentByRideId(@PathVariable Integer rideId) {
        PaymentDTO dto = paymentService.getPaymentByRideId(rideId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public PaymentDTO createPayment(@RequestBody PaymentDTO paymentDTO) {
        return paymentService.createPayment(paymentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id) {
        boolean deleted = paymentService.deletePayment(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
