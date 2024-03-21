package com.hatcher.haemo.recruitment.domain;

import com.hatcher.haemo.common.BaseEntity;
import com.hatcher.haemo.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "participant")
    private User participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "recruitment")
    private Recruitment recruitment;

    public void setParticipant(User participant) {
        this.participant = participant;
        participant.getParticipants().add(this);
    }

    public void setRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
        recruitment.getParticipants().add(this);
    }

    @Builder
    public Participant(User participant, Recruitment recruitment) {
        this.participant = participant;
        this.recruitment = recruitment;
    }
}
