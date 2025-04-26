package com.ridesharingappbackend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trip_durations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripDuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String duration;  // Storing duration as a string

}
