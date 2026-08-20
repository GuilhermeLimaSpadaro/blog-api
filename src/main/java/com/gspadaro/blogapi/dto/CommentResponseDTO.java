package com.gspadaro.blogapi.dto;

import java.time.Instant;

public record CommentResponseDTO(String id, String text, Instant date, String authorId, String postId) {
}
