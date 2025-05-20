package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.PaymentDTO;
import com.ridesharingappbackend.service.PaymentService;
import com.ridesharingappbackend.service.StripeService;
import com.stripe.model.PaymentIntent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final StripeService stripeService;

    public PaymentController(PaymentService paymentService, StripeService stripeService) {
        this.paymentService = paymentService;
        this.stripeService = stripeService;
    }

    @PostMapping("/create-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> body) {
        try {
            double amount = Double.parseDouble(body.get("amount").toString());
            PaymentIntent intent = stripeService.createPaymentIntent(amount, "eur");
            return ResponseEntity.ok(Map.of("clientSecret", intent.getClientSecret()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<PaymentDTO> getAllPayments(@RequestParam(required = false) Integer companyId, HttpServletRequest request) {
        if (companyId == null) {
            companyId = (Integer) request.getAttribute("companyId");
        }
        return paymentService.getAllPayments(companyId);
    }

    @GetMapping("/company")
    public List<PaymentDTO> getPaymentsByCompany(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return paymentService.getAllPayments(companyId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return paymentService.getPaymentByIdAndCompanyId(id, companyId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ride/{rideId}")
    public ResponseEntity<PaymentDTO> getPaymentByRideId(@PathVariable Integer rideId, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        PaymentDTO payment = paymentService.getPaymentByRideId(rideId, companyId);
        return payment != null ? ResponseEntity.ok(payment) : ResponseEntity.notFound().build();
    }


    @GetMapping("/my-payments")
    public List<PaymentDTO> getMyPayments(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        Integer userId = (Integer) request.getAttribute("userId");
        return paymentService.getPaymentsByCompanyAndUserId(companyId, userId);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getPaymentsByStatus(@PathVariable String status, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        try {
            List<PaymentDTO> payments = paymentService.getPaymentsByCompanyAndStatus(companyId, status);
            return ResponseEntity.ok(payments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ResponseEntity.ok(paymentService.createPayment(paymentDTO, companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable Integer id, @RequestBody PaymentDTO paymentDTO, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return paymentService.getPaymentByIdAndCompanyId(id, companyId).map(existing -> {
            paymentDTO.setId(id);
            return ResponseEntity.ok(paymentService.createPayment(paymentDTO, companyId));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        if (paymentService.deletePaymentByIdAndCompanyId(id, companyId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
