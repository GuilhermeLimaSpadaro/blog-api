package com.gspadaro.blogapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(@NotBlank String name,
                             @NotBlank @Email String email,
                             @NotBlank String phone,
                             @NotBlank @Size(min = 8) String password) {
}
