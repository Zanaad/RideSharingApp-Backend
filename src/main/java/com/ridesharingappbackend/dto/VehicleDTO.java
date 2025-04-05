package com.ridesharingappbackend.dto;

import com.ridesharingappbackend.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private Integer id;
    private Integer driverId;
    private VehicleType vehicleType;
    private String brand;
    private String model;
    private String plateNumber;
    private String color;
}
