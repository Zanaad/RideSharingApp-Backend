package com.ridesharingappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideTrackingDTO {
    private Integer id;
    private Integer rideId;
    private double latitude;
    private double longitude;
    private LocalDateTime timestamp;
}
