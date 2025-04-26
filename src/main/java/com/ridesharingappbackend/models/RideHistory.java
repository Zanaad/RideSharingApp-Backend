package com.ridesharingappbackend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ride_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime rideDate;

    private double fare;

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
