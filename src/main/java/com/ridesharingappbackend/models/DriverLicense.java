package com.ridesharingappbackend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "driver_licenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String licenseNumber;

    private String country;

    private String expirationDate;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;
}
