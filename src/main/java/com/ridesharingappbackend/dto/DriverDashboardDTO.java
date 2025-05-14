package com.ridesharingappbackend.dto;

import com.ridesharingappbackend.enums.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverDashboardDTO {
    public DriverDashboardDTO(String fullName2, String companyName, float rating2, String status2, int i,
            String licenseNumber2, LocalDateTime expiry) {
        //TODO Auto-generated constructor stub
    }
    private Integer driverId;
    private String fullName;
    private float rating;
    private DriverStatus status;
    private int completedRides;
    private String licenseNumber;
    private LocalDateTime licenseExpiry;
}
