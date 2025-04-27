// TripDurationRepository.java
package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.TripDuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripDurationRepository extends JpaRepository<TripDuration, Integer> {
}
