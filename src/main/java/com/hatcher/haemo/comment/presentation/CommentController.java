package com.hatcher.haemo.comment.presentation;

import com.hatcher.haemo.comment.application.CommentService;
import com.hatcher.haemo.comment.dto.CommentPostRequest;
import com.hatcher.haemo.common.BaseResponse;
import com.hatcher.haemo.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
