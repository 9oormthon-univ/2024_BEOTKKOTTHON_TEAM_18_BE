package com.hatcher.haemo.recruitment.repository;

import com.hatcher.haemo.common.enums.RecruitType;
import com.hatcher.haemo.recruitment.domain.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    List<Recruitment> findByStatusOrderByCreatedDateDesc(String status);
    List<Recruitment> findByTypeAndStatusEqualsOrderByCreatedDateDesc(RecruitType type, String status);
}
