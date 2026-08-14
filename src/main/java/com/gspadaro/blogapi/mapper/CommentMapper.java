package com.gspadaro.blogapi.mapper;

import com.gspadaro.blogapi.domain.Comment;
import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.CommentRequestDTO;
import com.gspadaro.blogapi.dto.CommentResponseDTO;

import java.util.List;

public class CommentMapper {

    public static Comment toEntity(CommentRequestDTO request, User user, Post post) {
        return new Comment(request.text(), user, post);
    }

    public static void updateEntity(Comment comment, CommentRequestDTO request) {
        comment.setText(request.text());
    }

    public static CommentResponseDTO toResponseDTO(Comment comment) {
        return new CommentResponseDTO(comment.getId(), comment.getText(), comment.getDate(), UserMapper.toDetailsDTO(comment.getAuthor()), comment.getPost().getId());
    }

    public static List<CommentResponseDTO> toList(List<Comment> commentList) {
        return commentList.stream().map(CommentMapper::toResponseDTO).toList();
    }
}
