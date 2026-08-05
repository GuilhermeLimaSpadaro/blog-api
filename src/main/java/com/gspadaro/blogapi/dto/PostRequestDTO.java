package com.gspadaro.blogapi.dto;

import java.time.LocalDate;
import java.util.List;

public record PostRequestDTO(LocalDate date, String title, String body, String authorId, List<CommentRequestDTO> comments) {
}
