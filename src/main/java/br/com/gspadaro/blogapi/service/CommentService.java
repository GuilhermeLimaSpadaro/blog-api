package br.com.gspadaro.blogapi.service;

import br.com.gspadaro.blogapi.dto.comment.CommentRequestDTO;
import br.com.gspadaro.blogapi.dto.comment.CommentResponseDTO;
import br.com.gspadaro.blogapi.exception.ResourceNotFoundException;
import br.com.gspadaro.blogapi.mapper.custom.CommentMapper;
import br.com.gspadaro.blogapi.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final UserService userService;
    private final PostService postService;

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, UserService userService, PostService postService) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userService = userService;
        this.postService = postService;
    }

    @Transactional
    public CommentResponseDTO create(CommentRequestDTO request) {
        logger.info("Create comment.");
        userService.findById(request.userId());
        postService.findById(request.postId());
        var comment = commentMapper.toEntity(request);
        var savedComment = commentRepository.save(comment);
        logger.info("Comment successfully created. CommentID: {}, PostID: {}, UserID: {}", savedComment.getId(), request.postId(), request.userId());
        return commentMapper.toResponseDTO(savedComment);
    }

    @Transactional
    public CommentResponseDTO findById(String commentId) {
        logger.info("Finding Comment.");
        var comment = commentRepository.findById(commentId).orElseThrow(() -> {
            logger.warn("Comment not found on findById. ID: {}", commentId);
            return new ResourceNotFoundException("Comment not found with ID: " + commentId);
        });
        logger.info("Post successfully found. ID: {}", commentId);
        return commentMapper.toResponseDTO(comment);
    }

    @Transactional
    public List<CommentResponseDTO> findByPostId(String postId) {
        logger.info("Finding all Comments of post.");
        var commentList = commentRepository.findByPostId(postId);
        logger.info("Post comments successfully found. ID: {}", postId);
        return commentMapper.toResponseListDTO(commentList);
    }

    @Transactional
    public CommentResponseDTO update(String commentId, CommentRequestDTO request) {
        logger.info("Update comment.");
        var comment = commentRepository.findById(commentId).orElseThrow(() -> {
            logger.warn("Comment not found on update. ID: {}", commentId);
            return new ResourceNotFoundException("Comment not found with ID: " + commentId);
        });
        userService.findById(request.userId());
        postService.findById(request.postId());
        commentMapper.toUpdateEntity(request, comment);
        var updatedComment = commentRepository.save(comment);
        logger.info("Comment successfully updated. ID: {}", commentId);
        return commentMapper.toResponseDTO(updatedComment);
    }

    @Transactional
    public void delete(String commentId) {
        logger.info("Delete comment.");
        var comment = commentRepository.findById(commentId).orElseThrow(() -> {
            logger.warn("Comment not found on delete. ID: {}", commentId);
            return new ResourceNotFoundException("Comment not found with ID: " + commentId);
        });
        commentRepository.delete(comment);
        logger.info("Comment successfully deleted. ID: {}", commentId);
    }
}
