package com.hatcher.haemo.recruitment.application;

import com.hatcher.haemo.common.BaseResponse;
import com.hatcher.haemo.common.exception.BaseException;
import com.hatcher.haemo.common.enums.RecruitType;
import com.hatcher.haemo.recruitment.domain.Recruitment;
import com.hatcher.haemo.recruitment.dto.RecruitmentPostRequest;
import com.hatcher.haemo.recruitment.repository.RecruitmentRepository;
import com.hatcher.haemo.user.application.UserService;
import com.hatcher.haemo.user.domain.User;
import com.hatcher.haemo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hatcher.haemo.common.constants.Constant.Recruitment.RECRUITING;
import static com.hatcher.haemo.common.enums.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RecruitmentRepository recruitmentRepository;

    // 모집글 등록
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> postRecruitment(RecruitmentPostRequest recruitmentPostRequest) throws BaseException{
        try {
            User leader = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            Recruitment recruitment = new Recruitment(recruitmentPostRequest.name(), leader, RecruitType.getEnumByName(recruitmentPostRequest.type()),
                    recruitmentPostRequest.participantLimit(), recruitmentPostRequest.contactUrl(), recruitmentPostRequest.description());
            recruitment.setStatus(RECRUITING);
            recruitmentRepository.save(recruitment);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
}
