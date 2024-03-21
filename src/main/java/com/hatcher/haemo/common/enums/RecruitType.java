package com.hatcher.haemo.common.enums;

import com.hatcher.haemo.common.exception.BaseException;
import java.util.Arrays;

import static com.hatcher.haemo.common.enums.BaseResponseStatus.WRONG_RECRUIT_TYPE;

public enum RecruitType {
    RESTAURANT("맛집"),
    CAFE("카페"),
    MOVIE("영화"),
    EXHIBITION("전시회"),
    BOARD_GAME("보드게임"),
    ESCAPE_GAME("방탈출"),
    COIN_KARAOKE("코인노래방"),
    DELIVERY_FOOD("배달음식");

    private final String description; // 설명 필드 추가

    RecruitType(String description) {
        this.description = description;
    }

    public String getDescription() { // 설명을 반환하는 메소드
        return description;
    }

    public static RecruitType getEnumByName(String name) throws BaseException{
        return Arrays.stream(RecruitType.values())
                .filter(recruitType -> recruitType.getDescription().equalsIgnoreCase(name))
                .findFirst()
                //.orElseThrow(() -> new IllegalArgumentException("찾을 수 없습니다."));
                .orElseThrow(() -> new BaseException(WRONG_RECRUIT_TYPE));
    }
}
