package com.ridesharingappbackend.models;

import com.ridesharingappbackend.enums.DriverStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private float rating;

    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


}


