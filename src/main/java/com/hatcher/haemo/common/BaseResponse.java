package com.hatcher.haemo.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hatcher.haemo.common.enums.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.hatcher.haemo.common.enums.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponse<T> {

    private final int code;

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;


    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(
                SUCCESS.getCode(),
                SUCCESS.isSuccess(),
                SUCCESS.getMessage(),
                null);
    }

    public static <T> BaseResponse<T> success(T result) {
        return new BaseResponse<>(
                SUCCESS.getCode(),
                SUCCESS.isSuccess(),
                SUCCESS.getMessage(),
                result);
    }

    public static <T> BaseResponse<T> failure(BaseResponseStatus status) {
        return new BaseResponse<>(
                status.getCode(),
                status.isSuccess(),
                status.getMessage(),
                null);
    }
}
