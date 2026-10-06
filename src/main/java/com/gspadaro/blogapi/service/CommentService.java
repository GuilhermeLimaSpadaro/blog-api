package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.dto.comment.CommentRequestDTO;
import com.gspadaro.blogapi.dto.comment.CommentResponseDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.mapper.custom.CommentMapper;
import com.gspadaro.blogapi.repository.CommentRepository;
import com.gspadaro.blogapi.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
    }

    public CommentResponseDTO create(CommentRequestDTO request) {
        logger.info("Create comment");
        var comment = commentMapper.toEntity(request);
        var savedComment = commentRepository.save(comment);
        logger.info("Comment successfully created. PostID: {}, AuthorID: {}", request.postId(), request.authorId());
        return commentMapper.toResponseDTO(savedComment);
    }

    public CommentResponseDTO findById(String commentId) {
        logger.info("Finding Comment.");
        var comment = commentRepository.findById(commentId).orElseThrow(() -> {
            logger.warn("Comment not found. ID: {}", commentId);
            return new ResourceNotFoundException("Comment not found");});
        logger.info("Comment successfully found. ID: {}", commentId);
        return commentMapper.toResponseDTO(comment);
    }

    public List<CommentResponseDTO> findAllCommentsByPostId(String postId) {
        logger.info("Finding all Comments of post");
        var post = postRepository.findById(postId).orElseThrow(() -> {
            logger.warn("Post not found, ID: {}", postId);
            return new ResourceNotFoundException("Post not found, ID: {}");});
        var commentList = commentRepository.findByPostId(postId);
        logger.info("Comments of post found.");
        return commentMapper.toResponseListDTO(commentList);
    }

    public CommentResponseDTO update(String commentId, CommentRequestDTO request) {
        logger.info("Update comment");
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
