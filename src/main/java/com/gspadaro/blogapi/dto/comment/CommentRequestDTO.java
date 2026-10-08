package com.gspadaro.blogapi.dto.comment;

import jakarta.validation.constraints.NotBlank;

public record CommentRequestDTO(@NotBlank String text, @NotBlank String userId, @NotBlank String postId) {
}
