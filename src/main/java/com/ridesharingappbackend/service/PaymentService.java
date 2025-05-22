package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.PaymentDTO;
import com.ridesharingappbackend.enums.PaymentStatus;
import com.ridesharingappbackend.exceptions.ResourceNotFoundException;
import com.ridesharingappbackend.mappers.PaymentMapper;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Payment;
import com.ridesharingappbackend.models.Ride;
import com.ridesharingappbackend.repository.PaymentRepository;
import com.ridesharingappbackend.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Shërbimi që menaxhon logjikën e biznesit për entitetin {@link Payment}.
 *
 * Ky shërbim ofron funksionalitete për:
 * <ul>
 *     <li>Marrjen e të gjitha pagesave të lidhura me një kompani</li>
 *     <li>Marrjen e një pagese sipas ID-së dhe ID-së së kompanisë</li>
 *     <li>Marrjen e një pagese për një udhëtim specifik</li>
 *     <li>Marrjen e pagesave të një përdoruesi brenda një kompanie</li>
 *     <li>Filtrimin e pagesave sipas statusit të pagesës</li>
 *     <li>Krijimin e një pagese të re për një udhëtim</li>
 *     <li>Fshirjen e një pagese duke verifikuar që i përket kompanisë</li>
 * </ul>
 *
 * Ky shërbim përdor {@link PaymentRepository} dhe {@link RideRepository} për të komunikuar me bazën e të dhënave,
 * si dhe {@link PaymentMapper} për të konvertuar të dhënat ndërmjet {@link Payment} dhe {@link PaymentDTO}.
 *
 * Në rastin kur tashmë ekziston një pagesë për një udhëtim të caktuar, ajo do të rikthehet pa krijuar një të re.
 *
 * @author Hare Luma
 * @since 2025-05-22
 * @version 1.0
 */

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final RideRepository rideRepo;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, RideRepository rideRepo) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.rideRepo = rideRepo;
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
        Payment existing = paymentRepository.findByCompanyIdAndRideId(companyId, dto.getRideId());
        if (existing != null) {
            return paymentMapper.toDTO(existing);
        }

        Payment payment = paymentMapper.toEntity(dto);

        Company company = new Company();
        company.setId(companyId);
        payment.setCompany(company);

        Ride ride = rideRepo.findById(dto.getRideId())
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
        payment.setRide(ride);
        payment.setUser(ride.getPassenger());

        return paymentMapper.toDTO(paymentRepository.save(payment));
    }

    public List<PaymentDTO> getPaymentsByCompanyUserAndStatus(Integer companyId, Integer userId, PaymentStatus status) {
        return paymentRepository.findByCompanyIdAndUserIdAndPaymentStatus(companyId, userId, status)
                .stream().map(paymentMapper::toDTO).collect(Collectors.toList());
    }

    public boolean deletePaymentByIdAndCompanyId(Integer id, Integer companyId) {
        return paymentRepository.findByIdAndCompanyId(id, companyId).map(payment -> {
            paymentRepository.delete(payment);
            return true;
        }).orElse(false);
    }
}