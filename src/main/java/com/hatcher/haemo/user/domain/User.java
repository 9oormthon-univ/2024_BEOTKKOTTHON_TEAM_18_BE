package com.hatcher.haemo.user.domain;

import com.hatcher.haemo.comment.domain.Comment;
import com.hatcher.haemo.common.BaseEntity;
import com.hatcher.haemo.notification.domain.Notification;
import com.hatcher.haemo.recruitment.domain.Participant;
import com.hatcher.haemo.recruitment.domain.Recruitment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

import static com.hatcher.haemo.common.constants.Constant.ACTIVE;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @OneToMany(mappedBy = "leader")
    @Where(clause = "status = 'ACTIVE'")
    private List<Recruitment> recruitments = new ArrayList<>(); // 해당 user가 leader로 있는 recruitment list

    @OneToMany(mappedBy = "writer")
    @Where(clause = "status = 'ACTIVE'")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "participant")
    @Where(clause = "status = 'ACTIVE'")
    private List<Participant> participants = new ArrayList<>(); // 해당 user가 participant로 있는 recruitment list(leader로 있는 모임은 제외)

    @OneToMany(mappedBy = "user")
    @Where(clause = "status = 'ACTIVE'")
    private List<Notification> notifications = new ArrayList<>();

    @Builder
    public User(String loginId, String password, String nickname) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
    }

    public void login() {
        this.setStatus(ACTIVE);
    }
}

