package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.service.PaymentService;
import com.ridesharingappbackend.service.StripeService;
import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    @Mock
    private StripeService stripeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPaymentIntent_success() throws Exception {
        double amount = 50.0;
        PaymentIntent mockIntent = new PaymentIntent();
        mockIntent.setClientSecret("test_client_secret");

        when(stripeService.createPaymentIntent(amount, "eur")).thenReturn(mockIntent);

        Map<String, Object> body = Map.of("amount", amount);
        ResponseEntity<Map<String, String>> response = paymentController.createPaymentIntent(body);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test_client_secret", response.getBody().get("clientSecret"));
    }

    @Test
    void createPaymentIntent_failure() throws Exception {
        double amount = 50.0;
        when(stripeService.createPaymentIntent(amount, "eur")).thenThrow(new RuntimeException("Stripe error"));

        Map<String, Object> body = Map.of("amount", amount);
        ResponseEntity<Map<String, String>> response = paymentController.createPaymentIntent(body);

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().get("error").contains("Stripe error"));
    }
}
