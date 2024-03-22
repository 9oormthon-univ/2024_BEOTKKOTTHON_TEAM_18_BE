package com.hatcher.haemo.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CommentDto(Long commentIdx,
                         String writer,
                         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm", timezone = "Asia/Seoul")
                         LocalDateTime createdDate,
                         String content) {}
