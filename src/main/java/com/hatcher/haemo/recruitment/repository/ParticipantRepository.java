package com.hatcher.haemo.recruitment.repository;

import com.hatcher.haemo.recruitment.domain.Participant;
import com.hatcher.haemo.recruitment.domain.Recruitment;
import com.hatcher.haemo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByRecruitmentAndStatusEquals(Recruitment recruitment, String status);
    Participant findByUserAndRecruitmentAndStatusEquals(User user, Recruitment recruitment, String status);
}
