package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.PaymentDTO;
import com.ridesharingappbackend.enums.PaymentStatus;
import com.ridesharingappbackend.mappers.PaymentMapper;
import com.ridesharingappbackend.models.Payment;
import com.ridesharingappbackend.models.Company;
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
    void getAllPayments_WithoutCompanyId_ReturnsList() {
        Payment payment = new Payment(); payment.setId(1);
        PaymentDTO dto = new PaymentDTO(); dto.setId(1);

        when(paymentRepository.findAll()).thenReturn(List.of(payment));
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        List<PaymentDTO> result = paymentService.getAllPayments(null);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getAllPayments_WithCompanyId_ReturnsFilteredList() {
        Payment payment = new Payment(); payment.setId(1);
        PaymentDTO dto = new PaymentDTO(); dto.setId(1);

        when(paymentRepository.findByCompanyId(5)).thenReturn(List.of(payment));
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        List<PaymentDTO> result = paymentService.getAllPayments(5);

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
    void getPaymentByIdAndCompanyId_WhenMatch_ReturnsDTO() {
        Company company = new Company(); company.setId(5);
        Payment payment = new Payment(); payment.setId(1); payment.setCompany(company);
        PaymentDTO dto = new PaymentDTO(); dto.setId(1);

        when(paymentRepository.findById(1)).thenReturn(Optional.of(payment));
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        Optional<PaymentDTO> result = paymentService.getPaymentByIdAndCompanyId(1, 5);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getPaymentByRideId_ReturnsDTO() {
        Payment payment = new Payment(); payment.setId(2);
        PaymentDTO dto = new PaymentDTO(); dto.setId(2);

        when(paymentRepository.findByRideId(10)).thenReturn(payment);
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        PaymentDTO result = paymentService.getPaymentByRideId(10);

        assertEquals(2, result.getId());
    }

    @Test
    void getPaymentByCompanyAndRideId_ReturnsDTO() {
        Payment payment = new Payment(); payment.setId(3);
        PaymentDTO dto = new PaymentDTO(); dto.setId(3);

        when(paymentRepository.findByCompanyAndRideId(4, 10)).thenReturn(payment);
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        PaymentDTO result = paymentService.getPaymentByCompanyAndRideId(4, 10);

        assertEquals(3, result.getId());
    }

    @Test
    void getPaymentsByCompanyAndUserId_ReturnsList() {
        Payment payment = new Payment(); payment.setId(4);
        PaymentDTO dto = new PaymentDTO(); dto.setId(4);

        when(paymentRepository.findByCompanyAndUserId(5, 100)).thenReturn(List.of(payment));
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        List<PaymentDTO> result = paymentService.getPaymentsByCompanyAndUserId(5, 100);

        assertEquals(1, result.size());
        assertEquals(4, result.get(0).getId());
    }

    @Test
    void getPaymentsByCompanyAndStatus_ValidStatus_ReturnsList() {
        Payment payment = new Payment(); payment.setId(7);
        PaymentDTO dto = new PaymentDTO(); dto.setId(7);

        when(paymentRepository.findByCompanyIdAndPaymentStatus(5, PaymentStatus.PENDING))
                .thenReturn(List.of(payment));
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        List<PaymentDTO> result = paymentService.getPaymentsByCompanyAndStatus(5, "pending");

        assertEquals(1, result.size());
        assertEquals(7, result.get(0).getId());
    }

    @Test
    void getPaymentsByCompanyAndStatus_InvalidStatus_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                paymentService.getPaymentsByCompanyAndStatus(1, "invalid_status"));

        assertEquals("Invalid payment status: invalid_status", exception.getMessage());
    }

    @Test
    void createPayment_SavesEntityAndReturnsDTO() {
        PaymentDTO dto = new PaymentDTO(); dto.setId(10);
        Payment payment = new Payment(); payment.setId(10);

        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        PaymentDTO result = paymentService.createPayment(dto);

        assertEquals(10, result.getId());
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
        when(paymentRepository.existsById(99)).thenReturn(false);

        boolean result = paymentService.deletePayment(99);

        assertFalse(result);
        verify(paymentRepository, never()).deleteById(any());
    }

    @Test
    void deletePaymentByIdAndCompanyId_WhenExists_ReturnsTrue() {
        Company company = new Company(); company.setId(5);
        Payment payment = new Payment(); payment.setId(8); payment.setCompany(company);

        when(paymentRepository.findById(8)).thenReturn(Optional.of(payment));
        doNothing().when(paymentRepository).delete(payment);

        boolean result = paymentService.deletePaymentByIdAndCompanyId(8, 5);

        assertTrue(result);
        verify(paymentRepository).delete(payment);
    }

    @Test
    void deletePaymentByIdAndCompanyId_WhenNotMatchingCompany_ReturnsFalse() {
        Company company = new Company(); company.setId(1);
        Payment payment = new Payment(); payment.setId(9); payment.setCompany(company);

        when(paymentRepository.findById(9)).thenReturn(Optional.of(payment));

        boolean result = paymentService.deletePaymentByIdAndCompanyId(9, 2);

        assertFalse(result);
        verify(paymentRepository, never()).delete(any());
    }

    @Test
    void deletePaymentByIdAndCompanyId_WhenNotExists_ReturnsFalse() {
        when(paymentRepository.findById(100)).thenReturn(Optional.empty());

        boolean result = paymentService.deletePaymentByIdAndCompanyId(100, 1);

        assertFalse(result);
    }
}
