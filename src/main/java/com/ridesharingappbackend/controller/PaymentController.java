package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.PaymentDTO;
import com.ridesharingappbackend.service.PaymentService;
import com.ridesharingappbackend.service.StripeService;
import com.stripe.model.PaymentIntent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Kontrolleri REST për menaxhimin e pagesave në aplikacionin e shpërndarjes së udhëtimeve.
 *
 * Kjo klasë ofron endpoint-e për:
 * <ul>
 *     <li>Krijimin e një Stripe PaymentIntent</li>
 *     <li>Marrjen e të gjitha pagesave për një kompani</li>
 *     <li>Marrjen e pagesave për një përdorues specifik</li>
 *     <li>Marrjen e një pagese sipas ID-së ose ID-së së udhëtimit</li>
 *     <li>Filtrimin e pagesave sipas statusit</li>
 *     <li>Krijimin, përditësimin dhe fshirjen e pagesave</li>
 * </ul>
 *
 * Ky kontroller përdor {@link PaymentService} për logjikën e biznesit të pagesave
 * dhe {@link StripeService} për integrim me Stripe API për krijimin e intent-eve të pagesave.
 *
 * ID e kompanisë dhe e përdoruesit merren nga atributet e kërkesës që vendosen nga një filtër autentikimi.
 *
 */

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Manage payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final StripeService stripeService;

    public PaymentController(PaymentService paymentService, StripeService stripeService) {
        this.paymentService = paymentService;
        this.stripeService = stripeService;
    }
    @Operation(summary = "Create Stripe payment intent")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client secret returned"),
            @ApiResponse(responseCode = "500", description = "Failed to create payment intent")
    })
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
    @Operation(summary = "Get all payments")
    @ApiResponse(responseCode = "200", description = "List of payments returned")
    @GetMapping
    public List<PaymentDTO> getAllPayments(@RequestParam(required = false) Integer companyId, HttpServletRequest request) {
        if (companyId == null) {
            companyId = (Integer) request.getAttribute("companyId");
        }
        return paymentService.getAllPayments(companyId);
    }

    @Operation(summary = "Get payments for current company")
    @ApiResponse(responseCode = "200", description = "List of company payments returned")
    @GetMapping("/company")
    public List<PaymentDTO> getPaymentsByCompany(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return paymentService.getAllPayments(companyId);
    }
    @Operation(summary = "Get a payment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment found"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return paymentService.getPaymentByIdAndCompanyId(id, companyId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Get payment by ride ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment for ride returned"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/ride/{rideId}")
    public ResponseEntity<PaymentDTO> getPaymentByRideId(@PathVariable Integer rideId, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        PaymentDTO payment = paymentService.getPaymentByRideId(rideId, companyId);
        return payment != null ? ResponseEntity.ok(payment) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get payments for current user")
    @ApiResponse(responseCode = "200", description = "List of user payments returned")
    @GetMapping("/my-payments")
    public List<PaymentDTO> getMyPayments(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        Integer userId = (Integer) request.getAttribute("userId");
        return paymentService.getPaymentsByCompanyAndUserId(companyId, userId);
    }
    @Operation(summary = "Get payments by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filtered payments returned"),
            @ApiResponse(responseCode = "400", description = "Invalid status")
    })
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
    @Operation(summary = "Create a new payment")
    @ApiResponse(responseCode = "200", description = "Payment created successfully")
    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return ResponseEntity.ok(paymentService.createPayment(paymentDTO, companyId));
    }
    @Operation(summary = "Update a payment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment updated successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable Integer id, @RequestBody PaymentDTO paymentDTO, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return paymentService.getPaymentByIdAndCompanyId(id, companyId).map(existing -> {
            paymentDTO.setId(id);
            return ResponseEntity.ok(paymentService.createPayment(paymentDTO, companyId));
        }).orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Delete a payment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Payment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        if (paymentService.deletePaymentByIdAndCompanyId(id, companyId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
