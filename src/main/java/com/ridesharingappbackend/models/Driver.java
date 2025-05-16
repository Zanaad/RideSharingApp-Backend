package com.ridesharingappbackend.models;

import com.ridesharingappbackend.enums.DriverStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float rating;

    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToOne(mappedBy = "driver")
    private Vehicle vehicle;

   //
}
