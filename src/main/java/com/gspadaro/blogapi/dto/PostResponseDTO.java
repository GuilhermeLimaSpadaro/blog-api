package com.gspadaro.blogapi.dto;

import java.time.Instant;

public record PostResponseDTO(String id, Instant date, String title, String body, UserDetailsDTO author) {
}
