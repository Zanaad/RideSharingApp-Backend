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

    private double pickupLat;
    private double pickupLng;

    private double dropOffLat;
    private double dropOffLng;

    private String pickupLocation;
    private String dropOffLocation;
    private LocalDateTime createdAt;

    private double distance;
    private double fare;
    private RideStatus status;
    private Integer companyId;

}
