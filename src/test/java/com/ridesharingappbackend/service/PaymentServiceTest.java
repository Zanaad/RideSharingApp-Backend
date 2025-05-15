package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.PaymentDTO;
import com.ridesharingappbackend.enums.PaymentStatus;
import com.ridesharingappbackend.mappers.PaymentMapper;
import com.ridesharingappbackend.models.Company;
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

    private final Integer companyId = 5;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPayments_ReturnsFilteredList() {
        Payment payment = new Payment(); payment.setId(1);
        PaymentDTO dto = new PaymentDTO(); dto.setId(1);

        when(paymentRepository.findByCompanyId(companyId)).thenReturn(List.of(payment));
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        List<PaymentDTO> result = paymentService.getAllPayments(companyId);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getPaymentByIdAndCompanyId_WhenExists_ReturnsDTO() {
        Payment payment = new Payment(); payment.setId(1);
        PaymentDTO dto = new PaymentDTO(); dto.setId(1);

        when(paymentRepository.findByIdAndCompanyId(1, companyId)).thenReturn(Optional.of(payment));
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        Optional<PaymentDTO> result = paymentService.getPaymentByIdAndCompanyId(1, companyId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getPaymentByIdAndCompanyId_WhenNotExists_ReturnsEmpty() {
        when(paymentRepository.findByIdAndCompanyId(99, companyId)).thenReturn(Optional.empty());

        Optional<PaymentDTO> result = paymentService.getPaymentByIdAndCompanyId(99, companyId);

        assertFalse(result.isPresent());
    }

    @Test
    void getPaymentByRideId_ReturnsDTO() {
        Payment payment = new Payment(); payment.setId(2);
        PaymentDTO dto = new PaymentDTO(); dto.setId(2);

        when(paymentRepository.findByCompanyIdAndRideId(companyId, 10)).thenReturn(payment);
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        PaymentDTO result = paymentService.getPaymentByRideId(10, companyId);

        assertEquals(2, result.getId());
    }

    @Test
    void getPaymentsByCompanyAndUserId_ReturnsList() {
        Payment payment = new Payment(); payment.setId(4);
        PaymentDTO dto = new PaymentDTO(); dto.setId(4);

        when(paymentRepository.findByCompanyIdAndUserId(companyId, 100)).thenReturn(List.of(payment));
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        List<PaymentDTO> result = paymentService.getPaymentsByCompanyAndUserId(companyId, 100);

        assertEquals(1, result.size());
        assertEquals(4, result.get(0).getId());
    }

    @Test
    void getPaymentsByCompanyAndStatus_ValidStatus_ReturnsList() {
        Payment payment = new Payment(); payment.setId(7);
        PaymentDTO dto = new PaymentDTO(); dto.setId(7);

        when(paymentRepository.findByCompanyIdAndPaymentStatus(companyId, PaymentStatus.PENDING))
                .thenReturn(List.of(payment));
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        List<PaymentDTO> result = paymentService.getPaymentsByCompanyAndStatus(companyId, "pending");

        assertEquals(1, result.size());
        assertEquals(7, result.get(0).getId());
    }

    @Test
    void getPaymentsByCompanyAndStatus_InvalidStatus_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                paymentService.getPaymentsByCompanyAndStatus(companyId, "invalid_status"));

        assertEquals("Invalid payment status: invalid_status", exception.getMessage());
    }

    @Test
    void createPayment_SavesEntityAndReturnsDTO() {
        PaymentDTO dto = new PaymentDTO(); dto.setId(10);
        Payment payment = new Payment(); payment.setId(10);

        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        PaymentDTO result = paymentService.createPayment(dto, companyId);

        assertEquals(10, result.getId());
        assertEquals(companyId, payment.getCompany().getId());
    }

    @Test
    void deletePaymentByIdAndCompanyId_WhenExists_ReturnsTrue() {
        Payment payment = new Payment(); payment.setId(8);
        Company company = new Company(); company.setId(companyId);
        payment.setCompany(company);

        when(paymentRepository.findByIdAndCompanyId(8, companyId)).thenReturn(Optional.of(payment));
        doNothing().when(paymentRepository).delete(payment);

        boolean result = paymentService.deletePaymentByIdAndCompanyId(8, companyId);

        assertTrue(result);
        verify(paymentRepository).delete(payment);
    }

    @Test
    void deletePaymentByIdAndCompanyId_WhenNotExists_ReturnsFalse() {
        when(paymentRepository.findByIdAndCompanyId(99, companyId)).thenReturn(Optional.empty());

        boolean result = paymentService.deletePaymentByIdAndCompanyId(99, companyId);

        assertFalse(result);
        verify(paymentRepository, never()).delete(any());
    }
}
