package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {
    List<Notifications> findByUserId(Integer userId);
}
