package com.hatcher.haemo.recruitment.repository;

import com.hatcher.haemo.recruitment.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
