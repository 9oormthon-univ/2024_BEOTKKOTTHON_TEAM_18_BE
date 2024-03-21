package com.hatcher.haemo.user.repository;

import com.hatcher.haemo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByUserIdxAndStatusEquals(Long userIdx, String status);
    boolean existsByNickname(String nickname);
}
