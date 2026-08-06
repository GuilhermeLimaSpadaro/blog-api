package com.gspadaro.blogapi.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDTO(String id, LocalDateTime date, String title, String body, UserDetailsDTO author,
                              List<CommentResponseDTO> comments) {
}
