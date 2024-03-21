package com.hatcher.haemo.recruitment.repository;

import com.hatcher.haemo.recruitment.domain.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
}
