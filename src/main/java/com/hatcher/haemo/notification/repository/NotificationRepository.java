package com.hatcher.haemo.notification.repository;

import com.hatcher.haemo.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
