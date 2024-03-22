package com.hatcher.haemo.recruitment.dto;

import com.hatcher.haemo.comment.dto.CommentDto;

import java.util.List;

public record RecruitResponse(RecruitmentDetailDto recruitment,
                              Integer commentCount,
                              List<CommentDto> commentList) {}
