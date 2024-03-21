package com.hatcher.haemo.common;

import com.hatcher.haemo.common.enums.BaseResponseStatus;
import org.springframework.web.server.ResponseStatusException;

public class BaseException extends ResponseStatusException {
    private final boolean success;
    private final Object data;

    public BaseException(BaseResponseStatus status) {
        super(status.getHttpStatus(), status.getMessage());
        this.success = false;
        this.data = null;
    }
}
