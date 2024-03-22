package com.hatcher.haemo.notification.domain;

import com.hatcher.haemo.common.BaseEntity;
import com.hatcher.haemo.recruitment.domain.Recruitment;
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
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "recruitment")
    private Recruitment recruitment;

    public void setUser(User user) {
        this.user = user;
        user.getNotifications().add(this);
    }

    @Builder
    public Notification(User user, Recruitment recruitment) {
        this.user = user;
        this.recruitment = recruitment;
    }
}
