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

    /*private String license_number;*/


 /*public DriverDashboardDTO(Integer driverId, String fullName, float rating, DriverStatus status) {
  this.driverId = driverId;
  this.fullName = fullName;
  this.rating = rating;
  this.status = status;

*//*  this.license_number = license_number;*//*

 }
    public Integer getDriverId() { return driverId; }
    public void setDriverId(Integer driverId) { this.driverId = driverId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public DriverStatus getStatus() { return status; }
    public void setStatus(DriverStatus status) { this.status = status; }*/
}
