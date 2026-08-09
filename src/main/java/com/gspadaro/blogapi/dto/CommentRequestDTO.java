package com.gspadaro.blogapi.dto;

public record CommentRequestDTO(String text, String authorId, String postId) {
}
