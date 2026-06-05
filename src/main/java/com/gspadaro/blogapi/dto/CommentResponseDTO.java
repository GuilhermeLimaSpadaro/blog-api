package com.gspadaro.blogapi.dto;

import com.gspadaro.blogapi.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponseDTO(String text, LocalDateTime date) {
    public static CommentResponseDTO from(Comment comment) {
        return new CommentResponseDTO(comment.getText(), comment.getDate());
    }
}
