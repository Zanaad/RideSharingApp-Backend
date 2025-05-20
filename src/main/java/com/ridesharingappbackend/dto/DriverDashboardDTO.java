package com.ridesharingappbackend.dto;

import com.ridesharingappbackend.enums.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverDashboardDTO {

    private Integer driverId;
    private String fullName;
    private float rating;
    private DriverStatus status;




}
