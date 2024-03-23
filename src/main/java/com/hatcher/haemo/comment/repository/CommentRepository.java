package com.hatcher.haemo.comment.repository;

import com.hatcher.haemo.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
