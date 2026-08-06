package com.gspadaro.blogapi.dto;

import java.time.LocalDate;
import java.util.List;

public record PostResponseDTO(String id, LocalDate date, String title, String body, UserDetailsDTO author,
                              List<CommentResponseDTO> comments) {
}
