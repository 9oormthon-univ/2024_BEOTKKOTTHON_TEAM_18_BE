package com.hatcher.haemo.user.application;

import com.hatcher.haemo.common.BaseException;
import com.hatcher.haemo.user.domain.User;
import com.hatcher.haemo.user.dto.LoginRequest;
import com.hatcher.haemo.user.dto.SignupRequest;
import com.hatcher.haemo.user.dto.TokenResponse;
import com.hatcher.haemo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hatcher.haemo.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthService authService;

    // 회원가입
    @Transactional(rollbackFor = Exception.class)
    public TokenResponse signup(SignupRequest signupRequest) throws BaseException {
        try {
            User newUser = signupRequest.toUser(encoder.encode(signupRequest.password()));
            userRepository.save(newUser);
            return new TokenResponse(authService.generateAccessToken(newUser.getUserIdx()));
        } catch (Exception e) {
            throw new BaseException(INTERNAL_SERVER_ERROR);
        }
    }

    // 로그인
    @Transactional(rollbackFor = Exception.class)
    public TokenResponse login(LoginRequest loginRequest) throws BaseException {
        try {
            User user = userRepository.findByLoginId(loginRequest.loginId()).orElseThrow(() -> new BaseException(LOGIN_ID_NOT_FOUND));
            if(!encoder.matches(loginRequest.password(), user.getPassword())) throw new BaseException(WRONG_PASSWORD);

            String accessToken = authService.generateAccessToken(user.getUserIdx());
            user.login();
            userRepository.save(user);

            return new TokenResponse(accessToken);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(INTERNAL_SERVER_ERROR);
        }
    }
}
