package com.ridesharingappbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ride_tracking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride;
}
