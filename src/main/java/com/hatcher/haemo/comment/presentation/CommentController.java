package com.hatcher.haemo.comment.presentation;

import com.hatcher.haemo.comment.application.CommentService;
import com.hatcher.haemo.comment.dto.CommentEditRequest;
import com.hatcher.haemo.comment.dto.CommentPostRequest;
import com.hatcher.haemo.common.BaseResponse;
import com.hatcher.haemo.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.hatcher.haemo.common.constants.RequestURI.comment;

@RestController
@RequiredArgsConstructor
@RequestMapping(comment)
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("")
    public BaseResponse<String> postComment(@RequestBody CommentPostRequest commentPostRequest) {
        try {
            return commentService.postComment(commentPostRequest);
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 댓글 수정
    @PatchMapping("/{commentIdx}/edit")
    public BaseResponse<String> editComment(@PathVariable Long commentIdx, @RequestBody CommentEditRequest commentEditRequest) {
        try {
            return commentService.editComment(commentIdx, commentEditRequest);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{commentIdx}/delete")
    public BaseResponse<String> deleteComment(@PathVariable Long commentIdx) {
        try {
            return commentService.deleteComment(commentIdx);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
