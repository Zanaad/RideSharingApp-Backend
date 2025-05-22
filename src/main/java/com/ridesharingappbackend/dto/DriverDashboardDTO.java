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
/**
 * DTO (Data Transfer Object) që përfaqëson të dhënat e dashboard-it të një shoferi.
 *
 * Kjo klasë përdoret për të transferuar informacionin kryesor që shfaqet në dashboard-in
 * e shoferit, si identifikuesi, emri i plotë, vlerësimi dhe statusi i tij aktual.
 *
 * Fushat e përfshira:
 * <ul>
 *   <li>{@code driverId} - ID unike e shoferit</li>
 *   <li>{@code fullName} - Emri dhe mbiemri i shoferit</li>
 *   <li>{@code rating} - Vlerësimi mesatar i shoferit nga pasagjerët</li>
 *   <li>{@code status} - Statusi aktual i shoferit (AKTIV, JOAKTIV, NË UDHTIM, etj.)</li>
 * </ul>
 *
 * Anotimet {@code @Getter}, {@code @Setter}, {@code @NoArgsConstructor} dhe {@code @AllArgsConstructor}
 * janë të ofruara nga Lombok për të gjeneruar automatikisht metodat getter/setter dhe konstruktorët.
 *
 * Kjo klasë mund të përdoret në shtresat e shërbimit dhe kontrollerit për të strukturuar përgjigjet HTTP.
 *
 * @author [Emri juaj]
 * @version 1.0
 * @since 2025-05-22
 */

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
