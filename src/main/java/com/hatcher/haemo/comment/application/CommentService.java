package com.hatcher.haemo.comment.application;

import com.hatcher.haemo.comment.domain.Comment;
import com.hatcher.haemo.comment.dto.CommentEditRequest;
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

import static com.hatcher.haemo.common.constants.Constant.INACTIVE;
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

    // [작성자] 댓글 수정
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> editComment(Long commentIdx, CommentEditRequest commentEditRequest) throws BaseException {
        try {
            Comment comment = commentRepository.findById(commentIdx).orElseThrow(() -> new BaseException(INVALID_COMMENT_IDX));
            User writer = userRepository.findByUserIdx(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            validateWriter(writer, comment);

            if (commentEditRequest.content() != null) {
                if (!commentEditRequest.content().equals("") && !commentEditRequest.content().equals(" "))
                    comment.modifyContent(commentEditRequest.content());
                else throw new BaseException(BLANK_COMMENT_CONTENT);
            }
            commentRepository.save(comment);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(INTERNAL_SERVER_ERROR);
        }
    }

    // [작성자] 댓글 삭제
    public BaseResponse<String> deleteComment(Long commentIdx) throws BaseException {
        try {
            Comment comment = commentRepository.findById(commentIdx).orElseThrow(() -> new BaseException(INVALID_COMMENT_IDX));
            User writer = userRepository.findByUserIdx(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            validateWriter(writer, comment);

            comment.delete();
            commentRepository.save(comment);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(INTERNAL_SERVER_ERROR);
        }
    }

    private static void validateWriter(User user, Comment comment) throws BaseException {
        if (!comment.getWriter().equals(user)) throw new BaseException(NO_COMMENT_WRITER);
        if (comment.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_COMMENT);
    }
}
