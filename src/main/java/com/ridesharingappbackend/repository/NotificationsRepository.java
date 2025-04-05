package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {
}
