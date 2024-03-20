package com.hatcher.haemo.user.dto;

import com.hatcher.haemo.user.domain.User;

public record SignupRequest(String nickname,
                            String loginId,
                            String password) {
    public User toUser(String encodedPassword) {
        return User.builder()
                .nickname(this.nickname)
                .loginId(this.loginId)
                .password(encodedPassword)
                .build();
    }
}
