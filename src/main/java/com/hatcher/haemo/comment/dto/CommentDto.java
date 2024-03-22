package com.hatcher.haemo.comment.dto;

import java.time.LocalDateTime;

public record CommentDto(Long commentIdx,
                         String writer,
                         LocalDateTime createdDate,
                         String content) {}
