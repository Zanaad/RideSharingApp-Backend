package com.ridesharingappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
    private Integer id;
    private Integer rideId;
    private Integer passengerId;
    private Integer driverId;
    private int rating;
    private String review;
    private LocalDateTime createdAt;
}
