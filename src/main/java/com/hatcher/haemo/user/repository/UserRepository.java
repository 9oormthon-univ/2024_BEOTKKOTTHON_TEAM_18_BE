package com.hatcher.haemo.user.repository;

import com.hatcher.haemo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
