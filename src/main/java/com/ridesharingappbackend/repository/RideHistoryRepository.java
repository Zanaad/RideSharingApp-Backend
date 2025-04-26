// RideHistoryRepository.java
package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.RideHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideHistoryRepository extends JpaRepository<RideHistory, Integer> {
}
