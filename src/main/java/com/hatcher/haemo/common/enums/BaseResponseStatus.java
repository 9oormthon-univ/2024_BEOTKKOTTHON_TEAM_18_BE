package com.hatcher.haemo.common.enums;

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
    INVALID_ACCESS_TOKEN(false, HttpStatus.BAD_REQUEST, "잘못된 access token 입니다."),
    NULL_ACCESS_TOKEN(false, HttpStatus.BAD_REQUEST, "access token을 입력해주세요."),
    INVALID_JWT_SIGNATURE(false, HttpStatus.BAD_REQUEST, "유효하지 않은 JWT 시그니처입니다."),
    EXPIRED_ACCESS_TOKEN(false, HttpStatus.BAD_REQUEST, "만료된 access token 입니다."),
    EXPIRED_REFRESH_TOKEN(false, HttpStatus.BAD_REQUEST, "만료된 refresh token 입니다."),
    EMPTY_JWT_CLAIM(false, HttpStatus.BAD_REQUEST, "JWT claims string이 비었습니다."),
    UNSUPPORTED_JWT_TOKEN(false, HttpStatus.BAD_REQUEST, "지원하지 않는 JWT 토큰 형식입니다."),
    INVALID_REFRESH_TOKEN(false, HttpStatus.BAD_REQUEST, "유효하지 않은 refresh token 입니다."),

    ACCESS_DENIED(false, HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    LOGIN_ID_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 loginId로 user를 찾을 수 없습니다."),
    INVALID_USER_IDX(false, HttpStatus.NOT_FOUND, "해당 userIdx로 user를 찾을 수 없습니다."),

    DUPLICATED_NICKNAME(false, HttpStatus.CONFLICT, "중복된 닉네임입니다."),
    DUPLICATED_LOGIN_ID(false, HttpStatus.CONFLICT, "중복된 로그인 아이디입니다."),

    // recruitment(2100-2199)
    WRONG_RECRUIT_TYPE(false, HttpStatus.NOT_FOUND, "해당 Recruit type을 찾을 수 없습니다."),
    INVALID_RECRUITMENT_IDX(false, HttpStatus.NOT_FOUND, "해당 recruitment idx로 recruitment를 찾을 수 없습니다."),
    NO_RECRUITMENT_LEADER(false, HttpStatus.CONFLICT, "해당 recruitment의 leader가 아닙니다."),
    BLANK_RECRUITMENT_NAME(false, HttpStatus.BAD_REQUEST, "recruitment name이 비었습니다."),
    BLANK_RECRUITMENT_TYPE(false, HttpStatus.BAD_REQUEST, "recruitment type이 비었습니다."),
    BLANK_PARTICIPANT_LIMIT(false, HttpStatus.BAD_REQUEST, "participant limit이 비었습니다."),
    BLANK_CONTACT_URL(false, HttpStatus.BAD_REQUEST, "contact url이 비었습니다."),
    BLANK_DESCRIPTION(false, HttpStatus.BAD_REQUEST, "description이 비었습니다."),
    LARGER_THAN_CURRENT_PARTICIPANT(false, HttpStatus.CONFLICT, "입력하신 모집 인원이 현재 참여 인원보다 작습니다."),
    NOT_RECRUITING_STATUS(false, HttpStatus.CONFLICT, "모집중 상태가 아닙니다."),
    ALREADY_DONE_RECRUITMENT(false, HttpStatus.CONFLICT, "해당 띱은 이미 모집인원에 도달했습니다."),
    LEADER_ROLE(false, HttpStatus.BAD_REQUEST, "리더는 띱 참여가 불가능합니다."),
    NOT_LEADER_ROLE(false, HttpStatus.BAD_REQUEST, "해당 띱의 리더가 아닙니다."),
    NOT_MEMBER_ROLE(false, HttpStatus.BAD_REQUEST, "해당 띱의 멤버가 아닙니다."),

    // comment(2200-2299)

    // notification(2300-2399)
    INVALID_NOTIFICATION_IDX(false, HttpStatus.NOT_FOUND, "해당 notification idx로 notification을 찾을 수 없습니다."),


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
    public HttpStatus getHttpStatus() {return httpStatus; }
}

