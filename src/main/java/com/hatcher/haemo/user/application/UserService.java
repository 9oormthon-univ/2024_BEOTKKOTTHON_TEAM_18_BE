package com.hatcher.haemo.user.application;

import com.hatcher.haemo.common.BaseException;
import com.hatcher.haemo.user.domain.User;
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
}
