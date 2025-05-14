package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.PaymentDTO;
import com.ridesharingappbackend.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<PaymentDTO> getAllPayments(
            @RequestParam(required = false) Integer companyId,
            HttpServletRequest request) {
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
    public ResponseEntity<PaymentDTO> getPaymentById(
            @PathVariable Integer id,
            HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return paymentService.getPaymentByIdAndCompanyId(id, companyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ride/{rideId}")
    public ResponseEntity<PaymentDTO> getPaymentByRideId(
            @PathVariable Integer rideId,
            HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        PaymentDTO payment = paymentService.getPaymentByCompanyAndRideId(companyId, rideId);
        return payment != null ? ResponseEntity.ok(payment) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public List<PaymentDTO> getPaymentsByUser(
            @PathVariable Integer userId,
            HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return paymentService.getPaymentsByCompanyAndUserId(companyId, userId);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getPaymentsByStatus(
            @PathVariable String status,
            HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        try {
            List<PaymentDTO> payments = paymentService.getPaymentsByCompanyAndStatus(companyId, status);
            return ResponseEntity.ok(payments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    public PaymentDTO createPayment(@RequestBody PaymentDTO paymentDTO) {
        return paymentService.createPayment(paymentDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<PaymentDTO> updatePayment(
            @PathVariable Integer id,
            @RequestBody PaymentDTO paymentDTO,
            HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return paymentService.getPaymentByIdAndCompanyId(id, companyId)
                .map(existing -> ResponseEntity.ok(paymentService.createPayment(paymentDTO)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<Void> deletePayment(
            @PathVariable Integer id,
            HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        if (paymentService.deletePaymentByIdAndCompanyId(id, companyId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}