package br.com.gspadaro.blogapi.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequestDTO(@NotBlank @Size(max = 200) String title,
                             @NotBlank String body,
                             @NotBlank String userId) {
}
