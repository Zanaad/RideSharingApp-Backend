package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {
    List<Notifications> findByUserId(Integer userId);

    List<Notifications> findByCompanyId(Integer companyId);

    Optional<Notifications> findByIdAndCompanyId(Integer id, Integer companyId);

    List<Notifications> findByUserIdAndCompanyId(Integer userId, Integer companyId);

    List<Notifications> findByUserIdAndCompanyIdAndIsReadFalse(Integer userId, Integer companyId);
}
