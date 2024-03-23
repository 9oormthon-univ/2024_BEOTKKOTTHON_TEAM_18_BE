package com.hatcher.haemo.comment.domain;

import com.hatcher.haemo.common.BaseEntity;
import com.hatcher.haemo.recruitment.domain.Recruitment;
import com.hatcher.haemo.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import static com.hatcher.haemo.common.constants.Constant.INACTIVE;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment")
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private User writer;

    @Column(nullable = false)
    private String content;

    @Builder
    public Comment(Recruitment recruitment, User writer, String content) {
        this.recruitment = recruitment;
        this.writer = writer;
        this.content = content;
    }

    public void setRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
        recruitment.getComments().add(this);
    }

    public void setWriter(User writer) {
        this.writer = writer;
        writer.getComments().add(this);
    }

    public void modifyContent(String content) {
        this.content = content;
    }

    public void delete() {
        this.setStatus(INACTIVE);
    }
}
