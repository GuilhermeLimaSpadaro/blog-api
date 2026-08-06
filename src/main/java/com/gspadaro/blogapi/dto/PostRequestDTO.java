package com.gspadaro.blogapi.dto;

import java.time.LocalDate;

public record PostRequestDTO(LocalDate date, String title, String body, String authorId) {
}
