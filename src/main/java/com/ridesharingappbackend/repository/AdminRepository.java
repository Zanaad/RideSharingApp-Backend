// AdminRepository.java
package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
}
