package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.dto.comment.CommentRequestDTO;
import com.gspadaro.blogapi.dto.comment.CommentResponseDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.mapper.custom.CommentMapper;
import com.gspadaro.blogapi.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public CommentResponseDTO create(CommentRequestDTO request) {
        var comment = commentMapper.toEntity(request);
        var savedComment = commentRepository.save(comment);
        return commentMapper.toResponseDTO(savedComment);
    }

    public CommentResponseDTO findById(String commentId) {
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        return commentMapper.toResponseDTO(comment);
    }

    public List<CommentResponseDTO> findAllCommentsByPostId(String postId) {
        var commentList = commentRepository.findByPostId(postId);
        return commentMapper.toResponseListDTO(commentList);
    }

    public CommentResponseDTO update(String commentId, CommentRequestDTO request) {
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        commentMapper.toUpdateEntity(request, comment);
        var updatedComment = commentRepository.save(comment);
        return commentMapper.toResponseDTO(updatedComment);
    }

    public void delete(String commentId) {
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        commentRepository.delete(comment);
    }
}
