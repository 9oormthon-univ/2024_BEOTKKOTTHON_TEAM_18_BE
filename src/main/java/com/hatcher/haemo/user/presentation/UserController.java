package com.hatcher.haemo.user.presentation;

import com.hatcher.haemo.common.BaseException;
import com.hatcher.haemo.common.BaseResponse;
import com.hatcher.haemo.user.application.UserService;
import com.hatcher.haemo.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.hatcher.haemo.common.constants.RequestURI.user;

@RestController
@RequiredArgsConstructor
@RequestMapping(user)
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping(value = "/signup")
    public BaseResponse<TokenResponse> signup(@RequestBody SignupRequest signupRequest) {
        return BaseResponse.success(userService.signup(signupRequest));
    }

    // 로그인
    @PostMapping("/login")
    public BaseResponse<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return BaseResponse.success(userService.login(loginRequest));
    }

    // 닉네임 중복 체크
    @PostMapping("/nickname")
    public BaseResponse<String> validateNickname(@RequestBody NicknameRequest nicknameRequest) {
        userService.validateNickname(nicknameRequest.nickname());
        return BaseResponse.success();
    }

    // 아이디 중복 체크
    @PostMapping("/loginId")
    public BaseResponse<String> validateLoginId(@RequestBody LoginIdRequest loginIdRequest) {
        userService.validateLoginId(loginIdRequest.loginId());
        return BaseResponse.success();
    }
}
