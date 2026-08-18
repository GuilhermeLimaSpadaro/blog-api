package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.Comment;
import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.CommentRequestDTO;
import com.gspadaro.blogapi.dto.CommentResponseDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.mapper.CommentMapper;
import com.gspadaro.blogapi.repository.CommentRepository;
import com.gspadaro.blogapi.repository.PostRepository;
import com.gspadaro.blogapi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentResponseDTO create(CommentRequestDTO request) {
        User user = userRepository.findById(request.authorId()).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        Post post = postRepository.findById(request.postId()).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        Comment comment = CommentMapper.toEntity(request, user, post);
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toResponseDTO(savedComment);
    }

    public CommentResponseDTO findById(String id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return CommentMapper.toResponseDTO(comment);
    }

    public CommentResponseDTO update(String id, CommentRequestDTO request) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        CommentMapper.updateEntity(comment, request);
        Comment updatedComment = commentRepository.save(comment);
        return CommentMapper.toResponseDTO(updatedComment);
    }

    public void delete(String id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        commentRepository.delete(comment);
    }


}
