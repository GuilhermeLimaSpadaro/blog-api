package com.gspadaro.blogapi.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentRequestDTO(@NotBlank String text, @NotBlank String authorId, @NotBlank String postId) {
}
