// DiscountCodeRepository.java
package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.DiscountCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountCodeRepository extends JpaRepository<DiscountCode, Integer> {
}
