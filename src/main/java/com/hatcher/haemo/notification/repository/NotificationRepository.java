package com.hatcher.haemo.notification.repository;

import com.hatcher.haemo.notification.domain.Notification;
import com.hatcher.haemo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserAndStatusEquals(User user, String status);
}
