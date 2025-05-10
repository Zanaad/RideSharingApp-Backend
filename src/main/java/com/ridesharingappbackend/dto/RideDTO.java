package com.ridesharingappbackend.dto;

import com.ridesharingappbackend.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideDTO {
    private Integer id;
    private Integer passengerId;
    private Integer driverId;
    private String pickupLocation;
    private String dropOffLocation;
    private double distance;
    private double estimatedFare;
    private RideStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private double fare;
    private LocalDateTime pickupTime;
}
