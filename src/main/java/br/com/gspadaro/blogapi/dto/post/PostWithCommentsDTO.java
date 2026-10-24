package br.com.gspadaro.blogapi.dto.post;

import br.com.gspadaro.blogapi.dto.comment.CommentResponseDTO;

import java.util.List;

public record PostWithCommentsDTO(PostResponseDTO post, List<CommentResponseDTO> comments) {
}
