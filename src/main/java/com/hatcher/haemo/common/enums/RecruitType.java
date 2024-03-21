package com.hatcher.haemo.common.enums;

import com.hatcher.haemo.common.BaseException;

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

    RecruitType(String name) {}

    public static RecruitType getEnumByName(String name) throws BaseException {
        return Arrays.stream(RecruitType.values())
                .filter(recruitType -> recruitType.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new BaseException(WRONG_RECRUIT_TYPE));
    }
}
