package com.gspadaro.blogapi.dto;

import java.time.LocalDateTime;

public record CommentResponseDTO(String text, LocalDateTime date, UserDetailsDTO author) {
}
