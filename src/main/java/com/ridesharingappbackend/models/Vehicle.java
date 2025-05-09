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

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}

