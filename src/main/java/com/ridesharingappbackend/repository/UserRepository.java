package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByCompanyId(Integer companyId);

    boolean existsByEmailAndCompanyId(String email, Integer companyId);

    Optional<User> findByIdAndCompanyId(Integer id, Integer companyId);

    Optional<User> findByEmailAndCompanyId(String email, Integer companyId);

    Optional<User>  findByEmail(String email);
}