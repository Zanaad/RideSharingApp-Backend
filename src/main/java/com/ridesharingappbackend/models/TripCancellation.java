package com.ridesharingappbackend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_cancellations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripCancellation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String reason;

    private LocalDateTime cancelledAt;

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private Ride ride;
}
