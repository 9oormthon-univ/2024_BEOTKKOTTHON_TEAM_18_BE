package com.hatcher.haemo.recruitment.application;

import com.hatcher.haemo.common.BaseResponse;
import com.hatcher.haemo.common.exception.BaseException;
import com.hatcher.haemo.common.enums.RecruitType;
import com.hatcher.haemo.recruitment.domain.Recruitment;
import com.hatcher.haemo.recruitment.dto.RecruitmentDto;
import com.hatcher.haemo.recruitment.dto.RecruitmentListResponse;
import com.hatcher.haemo.recruitment.dto.RecruitmentPostRequest;
import com.hatcher.haemo.recruitment.repository.RecruitmentRepository;
import com.hatcher.haemo.user.application.UserService;
import com.hatcher.haemo.user.domain.User;
import com.hatcher.haemo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            throw new BaseException(INTERNAL_SERVER_ERROR);
        }
    }

    public BaseResponse<RecruitmentListResponse> getRecruitmentList(String type) throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            List<RecruitmentDto> recruitmentList;
            if (type == null) { // 모집중인 띱 목록 조회
                recruitmentList = recruitmentRepository.findByStatusOrderByCreatedDateDesc(RECRUITING).stream()
                        .map(recruitment -> new RecruitmentDto(recruitment.getRecruitmentIdx(), recruitment.getType().getDescription(), recruitment.getName(),
                                recruitment.getLeader().getNickname(), recruitment.getParticipants().size(), recruitment.getParticipantLimit(), recruitment.getDescription(),
                                recruitment.getLeader().equals(user))).toList();
            } else { // 관심분야 띱 목록 조회
                recruitmentList = recruitmentRepository.findByTypeAndStatusEqualsOrderByCreatedDateDesc(RecruitType.getEnumByName(type), RECRUITING).stream()
                        .map(recruitment -> new RecruitmentDto(recruitment.getRecruitmentIdx(), recruitment.getType().getDescription(), recruitment.getName(),
                                recruitment.getLeader().getNickname(), recruitment.getParticipants().size(), recruitment.getParticipantLimit(), recruitment.getDescription(),
                                recruitment.getLeader().equals(user))).toList();
            }
            return new BaseResponse<>(new RecruitmentListResponse(recruitmentList));
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(INTERNAL_SERVER_ERROR);
        }
    }
}
