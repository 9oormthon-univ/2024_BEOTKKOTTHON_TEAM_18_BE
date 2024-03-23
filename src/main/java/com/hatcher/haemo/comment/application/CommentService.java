package com.hatcher.haemo.comment.application;

import com.hatcher.haemo.comment.domain.Comment;
import com.hatcher.haemo.comment.dto.CommentPostRequest;
import com.hatcher.haemo.comment.repository.CommentRepository;
import com.hatcher.haemo.common.BaseResponse;
import com.hatcher.haemo.common.exception.BaseException;
import com.hatcher.haemo.recruitment.domain.Recruitment;
import com.hatcher.haemo.recruitment.repository.RecruitmentRepository;
import com.hatcher.haemo.user.application.UserService;
import com.hatcher.haemo.user.domain.User;
import com.hatcher.haemo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hatcher.haemo.common.enums.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final RecruitmentRepository recruitmentRepository;
    private final CommentRepository commentRepository;

    // 댓글 등록
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> postComment(CommentPostRequest commentPostRequest) throws BaseException {
        try {
            User writer = userRepository.findByUserIdx(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Recruitment recruitment = recruitmentRepository.findById(commentPostRequest.recruitmentIdx()).orElseThrow(() -> new BaseException(INVALID_RECRUITMENT_IDX));
            Comment comment = new Comment(recruitment, writer, commentPostRequest.content());
            commentRepository.save(comment);
            comment.setRecruitment(recruitment);
            comment.setWriter(writer);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(INTERNAL_SERVER_ERROR);
        }
    }
}
