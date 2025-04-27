// RidePriorityRepository.java
package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.RidePriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RidePriorityRepository extends JpaRepository<RidePriority, Integer> {
}
