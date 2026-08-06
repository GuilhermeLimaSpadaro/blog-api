package com.gspadaro.blogapi.dto;

import java.time.LocalDate;

public record CommentResponseDTO(String text, LocalDate date, UserDetailsDTO author) {
}
