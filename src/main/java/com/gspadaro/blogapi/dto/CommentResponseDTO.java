package com.gspadaro.blogapi.dto;

import com.gspadaro.blogapi.domain.Comment;

import java.time.LocalDate;

public record CommentResponseDTO(String text, LocalDate date, UserResponseDTO author) {
    public static CommentResponseDTO from(Comment comment) {
        return new CommentResponseDTO(comment.getText(), comment.getDate(), UserResponseDTO.from(comment.getAuthor()));
    }
}
