package com.gspadaro.blogapi.dto;

import jakarta.validation.constraints.NotBlank;

public record PostRequestDTO(@NotBlank String title,
                             @NotBlank String body,
                             @NotBlank String authorId) {
}
