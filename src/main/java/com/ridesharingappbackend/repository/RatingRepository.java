package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
}
