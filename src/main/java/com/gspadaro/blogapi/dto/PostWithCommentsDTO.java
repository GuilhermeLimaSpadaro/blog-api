package com.gspadaro.blogapi.dto;

import java.util.List;

public record PostWithCommentsDTO(PostResponseDTO post, List<CommentResponseDTO> comment) {
}
