package com.gspadaro.blogapi.dto.post;

import jakarta.validation.constraints.NotBlank;

public record PostRequestDTO(@NotBlank String title,
                             @NotBlank String body,
                             @NotBlank String userId) {
}
