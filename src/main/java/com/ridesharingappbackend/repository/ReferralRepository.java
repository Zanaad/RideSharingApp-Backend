// ReferralRepository.java
package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Integer> {
}
