package com.gspadaro.blogapi.dto;

import java.time.LocalDateTime;

public record PostRequestDTO(String title, String body, String authorId) {
}
