package com.ridesharingappbackend.dto;

import com.ridesharingappbackend.enums.PaymentMethod;
import com.ridesharingappbackend.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Integer id;
    private Integer rideId;
    private Integer userId;
    private double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime createAt;
    private Integer companyId;
}

