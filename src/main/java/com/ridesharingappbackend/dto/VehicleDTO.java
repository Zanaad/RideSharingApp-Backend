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
    private Integer companyId;

    public Integer getId() {
        return id;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getModel() {
        return model;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

}
