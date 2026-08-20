package com.gspadaro.blogapi.mapper;

import com.gspadaro.blogapi.domain.Comment;
import com.gspadaro.blogapi.dto.CommentRequestDTO;
import com.gspadaro.blogapi.dto.CommentResponseDTO;

import java.util.List;

public class CommentMapper {

    public static Comment toEntity(CommentRequestDTO request) {
        return new Comment(request.text(), request.authorId(), request.postId());
    }

    public static void updateEntity(Comment comment, CommentRequestDTO request) {
        comment.setText(request.text());
    }

    public static CommentResponseDTO toResponseDTO(Comment comment) {
        return new CommentResponseDTO(comment.getId(), comment.getText(), comment.getDate(), comment.getAuthorId(), comment.getPostId());
    }

    public static List<CommentResponseDTO> toList(List<Comment> commentList) {
        return commentList.stream().map(CommentMapper::toResponseDTO).toList();
    }
}
