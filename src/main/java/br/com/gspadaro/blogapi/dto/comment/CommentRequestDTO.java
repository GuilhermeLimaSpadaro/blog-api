package br.com.gspadaro.blogapi.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequestDTO(@NotBlank @Size(max = 200) String text, @NotBlank String userId, @NotBlank String postId) {
}
