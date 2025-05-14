package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.PaymentDTO;
import com.ridesharingappbackend.mappers.PaymentMapper;
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

    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<PaymentDTO> getPaymentById(Integer id) {
        return paymentRepository.findById(id)
                .map(paymentMapper::toDTO);
    }

    public PaymentDTO getPaymentByRideId(Integer rideId) {
        return paymentMapper.toDTO(paymentRepository.findByRideId(rideId));
    }

    public PaymentDTO createPayment(PaymentDTO dto) {
        Payment payment = paymentMapper.toEntity(dto);
        return paymentMapper.toDTO(paymentRepository.save(payment));
    }

    public boolean deletePayment(Integer id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
