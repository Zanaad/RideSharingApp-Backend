package com.ridesharingappbackend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ride_priorities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RidePriority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String priority;  // Storing priority as a string

}
