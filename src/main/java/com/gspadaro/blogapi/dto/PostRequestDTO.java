package com.gspadaro.blogapi.dto;

import java.time.LocalDate;
import java.util.List;

public record PostRequestDTO(String id, LocalDate date, String title, String body, String authorId, List<CommentRequestDTO> comments) {
}
