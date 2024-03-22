package com.hatcher.haemo.recruitment.presentation;

import com.hatcher.haemo.common.exception.BaseException;
import com.hatcher.haemo.common.BaseResponse;
import com.hatcher.haemo.recruitment.application.RecruitmentService;
import com.hatcher.haemo.recruitment.dto.RecruitResponse;
import com.hatcher.haemo.recruitment.dto.RecruitmentEditRequest;
import com.hatcher.haemo.recruitment.dto.RecruitmentListResponse;
import com.hatcher.haemo.recruitment.dto.RecruitmentPostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.hatcher.haemo.common.constants.RequestURI.recruitment;

@RestController
@RequiredArgsConstructor
@RequestMapping(recruitment)
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    // 모집글 등록
    @PostMapping("")
    public BaseResponse<?> postRecruitment(@RequestBody RecruitmentPostRequest recruitmentPostRequest) {
       try {
            return recruitmentService.postRecruitment(recruitmentPostRequest);
        } catch(BaseException e) {
           return new BaseResponse<>(e.getStatus());
        }
    }

    // 모집중인 띱 목록 조회
    // 관심분야 띱 목록 조회
    // 참여중인 띱 목록 조회
    @GetMapping("")
    public BaseResponse<RecruitmentListResponse> getRecruitmentList(@RequestParam(required = false) String type, @RequestParam(required = false) boolean isParticipant) {
        try {
            return recruitmentService.getRecruitmentList(type, isParticipant);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 띱 상세 조회
    @GetMapping("/{recruitmentIdx}")
    public BaseResponse<RecruitResponse> getRecruitment(@PathVariable Long recruitmentIdx) {
        try {
            return recruitmentService.getRecruitment(recruitmentIdx);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 띱 수정
    @PatchMapping("/{recruitmentIdx}")
    public BaseResponse<?> editRecruitment(@PathVariable Long recruitmentIdx, @RequestBody RecruitmentEditRequest recruitmentEditRequest) {
        try {
            return recruitmentService.editRecruitment(recruitmentIdx, recruitmentEditRequest);
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 띱 참여하기
    @PostMapping("/{recruitmentIdx}")
    public BaseResponse<?> postRecruitment(@PathVariable Long recruitmentIdx) {
        try {
            return recruitmentService.participate(recruitmentIdx);
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
