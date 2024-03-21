package com.hatcher.haemo.user.presentation;

import com.hatcher.haemo.common.BaseException;
import com.hatcher.haemo.common.BaseResponse;
import com.hatcher.haemo.user.application.UserService;
import com.hatcher.haemo.user.dto.LoginRequest;
import com.hatcher.haemo.user.dto.NicknameRequest;
import com.hatcher.haemo.user.dto.SignupRequest;
import com.hatcher.haemo.user.dto.TokenResponse;
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
        try {
            return BaseResponse.success(userService.signup(signupRequest));
        } catch(BaseException e) {
            return BaseResponse.failure(e.getStatus());
        }
    }

    // 로그인
    @PostMapping("/login")
    public BaseResponse<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            return BaseResponse.success(userService.login(loginRequest));
        } catch(BaseException e) {
            return BaseResponse.failure(e.getStatus());
        }
    }

    // 닉네임 중복 체크
    @PostMapping("/nickname")
    public BaseResponse<String> validateNickname(@RequestBody NicknameRequest nicknameRequest) {
        try {
            userService.validateNickname(nicknameRequest.nickname());
            return BaseResponse.success();
        } catch (BaseException e){
            return BaseResponse.failure(e.getStatus());
        }
    }
}
