package com.gspadaro.blogapi.dto;

import java.time.Instant;
import java.util.List;

public record PostResponseDTO(String id, Instant date, String title, String body, UserDetailsDTO author,
                              List<CommentResponseDTO> comments) {
}
