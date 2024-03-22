package com.hatcher.haemo.recruitment.application;

import com.hatcher.haemo.comment.dto.CommentDto;
import com.hatcher.haemo.common.BaseResponse;
import com.hatcher.haemo.common.exception.BaseException;
import com.hatcher.haemo.common.enums.RecruitType;
import com.hatcher.haemo.recruitment.domain.Participant;
import com.hatcher.haemo.recruitment.domain.Recruitment;
import com.hatcher.haemo.recruitment.dto.*;
import com.hatcher.haemo.recruitment.repository.RecruitmentRepository;
import com.hatcher.haemo.user.application.UserService;
import com.hatcher.haemo.user.domain.User;
import com.hatcher.haemo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static com.hatcher.haemo.common.constants.Constant.Recruitment.DONE;
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
            recruitment.setLeader(leader);
            recruitmentRepository.save(recruitment);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(INTERNAL_SERVER_ERROR);
        }
    }

    // 띱 목록 조회
    public BaseResponse<RecruitmentListResponse> getRecruitmentList(String type, boolean isParticipant) throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            List<RecruitmentDto> recruitmentList = new ArrayList<>();
            if (isParticipant) { // 참여중인 띱 목록 조회
                // 리더로 있는 recruitment 목록 조회
                Stream<Recruitment> leaderRecruitmentStream = user.getRecruitments().stream();

                // 참여자로 있는 recruitment 목록 조회
                Stream<Recruitment> participantRecruitmentStream = user.getParticipants().stream()
                        .map(Participant::getRecruitment);

                List<RecruitmentDto> sortedRecruitmentList = Stream.concat(leaderRecruitmentStream, participantRecruitmentStream)
                        .sorted(Comparator.comparing(Recruitment::getCreatedDate).reversed())
                        .map(recruitment -> new RecruitmentDto(recruitment.getRecruitmentIdx(), recruitment.getType().getDescription(), recruitment.getName(),
                                recruitment.getLeader().getNickname(), recruitment.getParticipants().size(), recruitment.getParticipantLimit(), recruitment.getDescription(),
                                recruitment.getLeader().equals(user))).toList();
                recruitmentList.addAll(sortedRecruitmentList);
            } else {
                if (type == null) { // 모집중인 띱 목록 조회
                    recruitmentList = getRecruitmentList(recruitmentList, recruitmentRepository.findByStatusOrderByCreatedDateDesc(RECRUITING), user);
                } else { // 관심분야 띱 목록 조회
                    recruitmentList = getRecruitmentList(recruitmentList, recruitmentRepository.findByTypeAndStatusEqualsOrderByCreatedDateDesc(RecruitType.getEnumByName(type), RECRUITING), user);
                }
            }
            return new BaseResponse<>(new RecruitmentListResponse(recruitmentList));
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(INTERNAL_SERVER_ERROR);
        }
    }

    private List<RecruitmentDto> getRecruitmentList(List<RecruitmentDto> recruitmentList, List<Recruitment> recruitmentRepositoryList, User user) {
        recruitmentList = recruitmentRepositoryList.stream()
                .map(recruitment -> new RecruitmentDto(recruitment.getRecruitmentIdx(), recruitment.getType().getDescription(), recruitment.getName(),
                        recruitment.getLeader().getNickname(), recruitment.getParticipants().size(), recruitment.getParticipantLimit(), recruitment.getDescription(),
                        recruitment.getLeader().equals(user))).toList();
        return recruitmentList;
    }

    // 띱 상세 조회
    public BaseResponse<RecruitResponse> getRecruitment(Long recruitmentIdx) throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Recruitment recruitment = recruitmentRepository.findById(recruitmentIdx).orElseThrow(() -> new BaseException(INVALID_RECRUITMENT_IDX));

            RecruitmentDetailDto recruitmentDetailDto = new RecruitmentDetailDto(recruitment.getRecruitmentIdx(), recruitment.getType().getDescription(), recruitment.getName(),
                    recruitment.getLeader().getNickname(), recruitment.getParticipants().size()+1, recruitment.getParticipantLimit(), recruitment.getDescription(),
                    recruitment.getLeader().equals(user),  recruitment.getStatus().equals(RECRUITING));
            Integer commentCount = recruitment.getComments().size();
            List<CommentDto> commentList = recruitment.getComments().stream()
                    .map(comment -> new CommentDto(comment.getCommentIdx(), comment.getWriter().getNickname(), comment.getCreatedDate(), comment.getContent())).toList(); //TODO: 생성일자 반환 형식 수정 필요

            RecruitResponse recruitResponse = new RecruitResponse(recruitmentDetailDto, commentCount, commentList);
            return new BaseResponse<>(recruitResponse);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(INTERNAL_SERVER_ERROR);
        }
    }

    // 띱 수정
    public BaseResponse<String> editRecruitment(Long recruitmentIdx, RecruitmentEditRequest recruitmentEditRequest) throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Recruitment recruitment = recruitmentRepository.findById(recruitmentIdx).orElseThrow(() -> new BaseException(INVALID_RECRUITMENT_IDX));

            validateWriter(user, recruitment);
            if (recruitment.getStatus().equals(DONE)) throw new BaseException(NOT_RECRUITING_STATUS);

            if (recruitmentEditRequest.name() != null) {
                if (!recruitmentEditRequest.name().equals("") && !recruitmentEditRequest.name().equals(" "))
                    recruitment.modifyName(recruitmentEditRequest.name());
                else throw new BaseException(BLANK_RECRUITMENT_NAME);
            }
            if (recruitmentEditRequest.type() != null) {
                if (!recruitmentEditRequest.type().equals("") && !recruitmentEditRequest.type().equals(" "))
                    recruitment.modifyType(RecruitType.getEnumByName(recruitmentEditRequest.type()));
                else throw new BaseException(BLANK_RECRUITMENT_TYPE);
            }
            if (recruitmentEditRequest.participantLimit() != null) {
                if (recruitmentEditRequest.participantLimit() < recruitment.getParticipants().size()+1)
                    throw new BaseException(LARGER_THAN_CURRENT_PARTICIPANT);
                else if (recruitmentEditRequest.participantLimit() == recruitment.getParticipants().size()) {
                    recruitment.setStatus(DONE);
                } else recruitment.modifyParticipantLimit(recruitmentEditRequest.participantLimit());
            }
            if (recruitmentEditRequest.contactUrl() != null) {
                if (!recruitmentEditRequest.contactUrl().equals("") && !recruitmentEditRequest.contactUrl().equals(" "))
                    recruitment.modifyContactUrl(recruitmentEditRequest.contactUrl());
                else throw new BaseException(BLANK_CONTACT_URL);
            }
            if (recruitmentEditRequest.description() != null) {
                if (!recruitmentEditRequest.description().equals("") && !recruitmentEditRequest.description().equals(" "))
                    recruitment.modifyDesscription(recruitmentEditRequest.description());
                else throw new BaseException(BLANK_DESCRIPTION);
            }
            recruitmentRepository.save(recruitment);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(INTERNAL_SERVER_ERROR);
        }
    }

    private void validateWriter(User user, Recruitment recruitment) throws BaseException {
        if (!recruitment.getLeader().equals(user)) throw new BaseException(NO_RECRUITMENT_LEADER);
    }
}
