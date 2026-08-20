package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.Comment;
import com.gspadaro.blogapi.dto.CommentRequestDTO;
import com.gspadaro.blogapi.dto.CommentResponseDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.mapper.CommentMapper;
import com.gspadaro.blogapi.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentResponseDTO create(CommentRequestDTO request) {
        Comment comment = CommentMapper.toEntity(request);
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toResponseDTO(savedComment);
    }

    public CommentResponseDTO findById(String commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return CommentMapper.toResponseDTO(comment);
    }

    public List<CommentResponseDTO> findAllCommentsByPostId(String postId){
        List<Comment> commentsList = commentRepository.findByPostId(postId);
        return CommentMapper.toList(commentsList);
    }

    public CommentResponseDTO update(String commentId, CommentRequestDTO request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        CommentMapper.updateEntity(comment, request);
        Comment updatedComment = commentRepository.save(comment);
        return CommentMapper.toResponseDTO(updatedComment);
    }

    public void delete(String commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        commentRepository.delete(comment);
    }


}
