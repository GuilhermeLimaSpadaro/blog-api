package br.com.gspadaro.blogapi.service;

import br.com.gspadaro.blogapi.dto.comment.CommentRequestDTO;
import br.com.gspadaro.blogapi.dto.comment.CommentResponseDTO;
import br.com.gspadaro.blogapi.exception.ResourceNotFoundException;
import br.com.gspadaro.blogapi.mapper.custom.CommentMapper;
import br.com.gspadaro.blogapi.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public CommentResponseDTO create(CommentRequestDTO request) {
        logger.info("Create comment.");
        userService.findById(request.userId());
        postService.findById(request.postId());
        var comment = commentMapper.toEntity(request);
        var savedComment = commentRepository.save(comment);
        logger.info("Comment successfully created. CommentID: {}, PostID: {}, UserID: {}", savedComment.getId(), request.postId(), request.userId());
        return commentMapper.toResponseDTO(savedComment);
    }

    public CommentResponseDTO findById(String commentId) {
        logger.info("Finding Comment. ID: {}", commentId);
        var comment = commentRepository.findById(commentId).orElseThrow(() -> {
            logger.warn("Comment not found. ID: {}", commentId);
            return new ResourceNotFoundException("Comment not found");
        });
        logger.info("Comment successfully found.");
        return commentMapper.toResponseDTO(comment);
    }

    public List<CommentResponseDTO> findByPostId(String postId) {
        logger.info("Finding all Comments of post, ID: {}", postId);
        var commentList = commentRepository.findByPostId(postId);
        logger.info("Post comments successfully found.");
        return commentMapper.toResponseListDTO(commentList);
    }

    public CommentResponseDTO update(String commentId, CommentRequestDTO request) {
        logger.info("Update comment. ID: {}", commentId);
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        userService.findById(request.userId());
        postService.findById(request.postId());
        commentMapper.toUpdateEntity(request, comment);
        var updatedComment = commentRepository.save(comment);
        logger.info("Comment successfully updated");
        return commentMapper.toResponseDTO(updatedComment);
    }

    public void delete(String commentId) {
        logger.info("Delete comment. ID: {}", commentId);
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        commentRepository.delete(comment);
        logger.info("Comment successfully deleted.");
    }
}
