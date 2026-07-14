package com.gspadaro.blogapi.dto;

import java.time.Instant;

public record CommentResponseDTO(String id, String text, Instant date, UserDetailsDTO author) {
}
