package com.gspadaro.blogapi.dto.comment;

import java.time.Instant;

public record CommentResponseDTO(String id, String text, Instant date, String userId, String postId) {
}
