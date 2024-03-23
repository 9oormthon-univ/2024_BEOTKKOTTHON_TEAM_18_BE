package com.hatcher.haemo.recruitment.domain;

import com.hatcher.haemo.comment.domain.Comment;
import com.hatcher.haemo.common.BaseEntity;
import com.hatcher.haemo.common.enums.RecruitType;
import com.hatcher.haemo.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recruitmentIdx;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "leader")
    private User leader; // 모집자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecruitType type;

    @Column(nullable = false)
    private Integer participantLimit;

    private String contactUrl;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "recruitment")
    @Where(clause = "status = 'ACTIVE'")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "recruitment")
    @Where(clause = "status = 'ACTIVE'")
    private List<Participant> participants = new ArrayList<>(); // 띱을 나가도 이 리스트에는 있고 participant inactive 처리

    @Builder
    public Recruitment(String name, User leader, RecruitType type, Integer participantLimit, String contactUrl, String description) {
        this.name = name;
        this.leader = leader;
        this.type = type;
        this.participantLimit = participantLimit;
        this.contactUrl = contactUrl;
        this.description = description;
    }

    public void setLeader(User leader) {
        this.leader = leader;
        leader.getRecruitments().add(this);
    }

    public void modifyName(String name) {
        this.name = name;
    }

    public void modifyType(RecruitType type) {
        this.type = type;
    }

    public void modifyContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public void modifyDesscription(String description) {
        this.description = description;
    }

    public void modifyParticipantLimit(Integer participantLimit) {
        this.participantLimit = participantLimit;
    }
}
