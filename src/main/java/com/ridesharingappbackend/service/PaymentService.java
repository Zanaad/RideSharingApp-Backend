package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.PaymentDTO;
import com.ridesharingappbackend.enums.PaymentStatus;
import com.ridesharingappbackend.mappers.PaymentMapper;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Payment;
import com.ridesharingappbackend.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    public List<PaymentDTO> getAllPayments(Integer companyId) {
        return paymentRepository.findByCompanyId(companyId).stream().map(paymentMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<PaymentDTO> getPaymentByIdAndCompanyId(Integer id, Integer companyId) {
        return paymentRepository.findByIdAndCompanyId(id, companyId).map(paymentMapper::toDTO);
    }

    public PaymentDTO getPaymentByRideId(Integer rideId, Integer companyId) {
        Payment payment = paymentRepository.findByCompanyIdAndRideId(companyId, rideId);
        return paymentMapper.toDTO(payment);
    }

    public List<PaymentDTO> getPaymentsByCompanyAndUserId(Integer companyId, Integer userId) {
        return paymentRepository.findByCompanyIdAndUserId(companyId, userId).stream().map(paymentMapper::toDTO).collect(Collectors.toList());
    }

    public List<PaymentDTO> getPaymentsByCompanyAndStatus(Integer companyId, String status) {
        try {
            PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
            return paymentRepository.findByCompanyIdAndPaymentStatus(companyId, paymentStatus).stream().map(paymentMapper::toDTO).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }
    }

    public PaymentDTO createPayment(PaymentDTO dto, Integer companyId) {
        Payment payment = paymentMapper.toEntity(dto);
        Company company = new Company();
        company.setId(companyId);
        payment.setCompany(company);
        return paymentMapper.toDTO(paymentRepository.save(payment));
    }

    public boolean deletePaymentByIdAndCompanyId(Integer id, Integer companyId) {
        return paymentRepository.findByIdAndCompanyId(id, companyId).map(payment -> {
            paymentRepository.delete(payment);
            return true;
        }).orElse(false);
    }
}