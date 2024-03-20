package com.hatcher.haemo.user.presentation;

import com.hatcher.haemo.common.BaseException;
import com.hatcher.haemo.common.BaseResponse;
import com.hatcher.haemo.user.application.UserService;
import com.hatcher.haemo.user.dto.SignupRequest;
import com.hatcher.haemo.user.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hatcher.haemo.common.constants.RequestURI.user;

@RestController
@RequiredArgsConstructor
@RequestMapping(user)
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping(value = "/signup")
    public BaseResponse<TokenResponse> signup(@RequestBody SignupRequest signupRequest) {
        try {
            return BaseResponse.success(userService.signup(signupRequest));
        } catch(BaseException e) {
            return BaseResponse.failure(e.getStatus());
        }
    }
}
