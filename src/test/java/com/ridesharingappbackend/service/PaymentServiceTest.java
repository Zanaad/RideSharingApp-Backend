package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.PaymentDTO;
import com.ridesharingappbackend.mappers.PaymentMapper;
import com.ridesharingappbackend.models.Payment;
import com.ridesharingappbackend.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPayments_ReturnsListOfDTOs() {
        Payment payment = new Payment(); payment.setId(1);
        PaymentDTO dto = new PaymentDTO(); dto.setId(1);

        when(paymentRepository.findAll()).thenReturn(List.of(payment));
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        List<PaymentDTO> result = paymentService.getAllPayments();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getPaymentById_WhenExists_ReturnsDTO() {
        Payment payment = new Payment(); payment.setId(1);
        PaymentDTO dto = new PaymentDTO(); dto.setId(1);

        when(paymentRepository.findById(1)).thenReturn(Optional.of(payment));
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        Optional<PaymentDTO> result = paymentService.getPaymentById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getPaymentById_WhenNotExists_ReturnsEmpty() {
        when(paymentRepository.findById(99)).thenReturn(Optional.empty());

        Optional<PaymentDTO> result = paymentService.getPaymentById(99);

        assertFalse(result.isPresent());
    }

    @Test
    void getPaymentByRideId_ReturnsDTO() {
        Payment payment = new Payment(); payment.setId(1);
        PaymentDTO dto = new PaymentDTO(); dto.setId(1);

        when(paymentRepository.findByRideId(5)).thenReturn(payment);
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        PaymentDTO result = paymentService.getPaymentByRideId(5);

        assertEquals(1, result.getId());
    }

    @Test
    void createPayment_SavesEntityAndReturnsDTO() {
        PaymentDTO dto = new PaymentDTO(); dto.setId(1);
        Payment payment = new Payment(); payment.setId(1);

        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        PaymentDTO result = paymentService.createPayment(dto);

        assertEquals(1, result.getId());
    }

    @Test
    void deletePayment_WhenExists_ReturnsTrue() {
        when(paymentRepository.existsById(1)).thenReturn(true);
        doNothing().when(paymentRepository).deleteById(1);

        boolean result = paymentService.deletePayment(1);

        assertTrue(result);
        verify(paymentRepository).deleteById(1);
    }

    @Test
    void deletePayment_WhenNotExists_ReturnsFalse() {
        when(paymentRepository.existsById(2)).thenReturn(false);

        boolean result = paymentService.deletePayment(2);

        assertFalse(result);
        verify(paymentRepository, never()).deleteById(any());
    }
}
