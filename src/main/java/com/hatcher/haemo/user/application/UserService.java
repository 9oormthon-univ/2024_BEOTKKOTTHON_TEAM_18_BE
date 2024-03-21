package com.hatcher.haemo.user.application;

import com.hatcher.haemo.common.exception.*;
import com.hatcher.haemo.user.domain.User;
import com.hatcher.haemo.user.dto.*;
import com.hatcher.haemo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.hatcher.haemo.common.enums.BaseResponseStatus.*;

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
    public TokenResponse login(LoginRequest loginRequest) throws BaseException{
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

    // 닉네임 중복 체크
    public void validateNickname(String nickname) throws BaseException {
        if(userRepository.existsByNickname(nickname)) throw new BaseException(DUPLICATED_NICKNAME);
    }

    // 아이디 중복 체크
    public void validateLoginId(String loginId) throws BaseException {
        if(userRepository.existsByLoginId(loginId)) throw new BaseException(DUPLICATED_LOGIN_ID);
    }

    public User getUserByUserIdx(Long userIdx) {
        if(userIdx == null) return null;
        else {
            Optional<User> user = userRepository.findByUserIdx(userIdx);
            return user.orElse(null);
        }
    }

    // 회원만
    public Long getUserIdxWithValidation() throws BaseException {
        Long userIdx = authService.getUserIdx();
        if (userIdx == null) throw new BaseException(NULL_ACCESS_TOKEN);
        return userIdx;
    }
}
