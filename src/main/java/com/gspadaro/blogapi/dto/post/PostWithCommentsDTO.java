package com.gspadaro.blogapi.dto.post;

import com.gspadaro.blogapi.dto.comment.CommentResponseDTO;

import java.util.List;

public record PostWithCommentsDTO(PostResponseDTO post, List<CommentResponseDTO> comment) {
}
