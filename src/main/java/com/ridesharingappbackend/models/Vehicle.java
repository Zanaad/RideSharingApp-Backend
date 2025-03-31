package com.ridesharingappbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "driver_id", unique = true)
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String brand;
    private String model;

    @Column(unique = true, nullable = false)
    private String plateNumber;

    private String color;
}

enum VehicleType {
    SEDAN, SUV, VAN, MOTORBIKE
}
