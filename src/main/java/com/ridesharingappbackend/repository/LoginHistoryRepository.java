// LoginHistoryRepository.java
package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {
}
