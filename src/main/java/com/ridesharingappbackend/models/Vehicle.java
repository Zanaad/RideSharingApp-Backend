package com.ridesharingappbackend.models;

import com.ridesharingappbackend.enums.VehicleType;
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

    private String brand;

    private String color;

    private String model;

    @Column(name = "plate_number", nullable = false, unique = true)
    private String plateNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
    private VehicleType vehicleType;

    @OneToOne
    @JoinColumn(name = "driver_id", unique = true)
    private Driver driver;
}
