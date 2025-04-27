// TripCancellationRepository.java
package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.TripCancellation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripCancellationRepository extends JpaRepository<TripCancellation, Integer> {
}
