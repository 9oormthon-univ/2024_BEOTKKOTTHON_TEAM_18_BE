package com.hatcher.haemo.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000: 요청 성공
     */
    SUCCESS(true, HttpStatus.OK, "요청에 성공하였습니다."),


    /**
     * 2000: Request 오류
     */
    // user(2000-2049)
    WRONG_PASSWORD(false, HttpStatus.BAD_REQUEST, "잘못된 password 입니다."),

    LOGIN_ID_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 loginId로 user를 찾을 수 없습니다."),
    INVALID_USER_IDX(false, HttpStatus.NOT_FOUND, "해당 userIdx로 user를 찾을 수 없습니다."),

    DUPLICATED_NICKNAME(false, HttpStatus.CONFLICT, "중복된 닉네임입니다."),

    // recruitment(2100-2199)

    // comment(2200-2299)


    /**
     * 3000: Response 오류
     */
    // user(3000-3099)

    // recruitment(3100-3199)

    // comment(3200-3299)


    /**
     * 4000: DB, Server 오류
     */
    INTERNAL_SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    private final boolean isSuccess;
    private final HttpStatus httpStatus;
    private final String message;

    BaseResponseStatus(boolean isSuccess, HttpStatus status, String message) {
        this.isSuccess = isSuccess;
        this.httpStatus = status;
        this.message = message;
    }

    public int getCode() {
        return httpStatus.value();
    }
}

