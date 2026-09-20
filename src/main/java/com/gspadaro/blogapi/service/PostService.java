package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.dto.post.PostRequestDTO;
import com.gspadaro.blogapi.dto.post.PostResponseDTO;
import com.gspadaro.blogapi.dto.post.PostWithCommentsDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.mapper.custom.PostMapper;
import com.gspadaro.blogapi.mapper.custom.UserMapper;
import com.gspadaro.blogapi.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final UserService userService;
    private final CommentService commentService;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    public PostService(PostRepository postRepository, PostMapper postMapper, UserMapper userMapper, UserService userService, CommentService commentService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userMapper = userMapper;
        this.userService = userService;
        this.commentService = commentService;
    }

    public PostResponseDTO create(PostRequestDTO request) {
        logger.info("Create Post");
        var post = postMapper.toEntity(request);
        var user = userService.findById(post.getAuthorId());
        var savedPost = postRepository.save(post);
        logger.info("Post successfully created. ID: {}", post.getId());
        return postMapper.toResponseDTO(savedPost, userMapper.toDetailsDTO(user));
    }

    public PostResponseDTO findById(String postId) {
        logger.info("Finding Post. ID: {}", postId);
        var post = postRepository.findById(postId).orElseThrow(() -> {
            logger.warn("Post not found. ID: {}", postId);
            return new ResourceNotFoundException("Post not found");
        });
        logger.info("Post successfully found.");
        var user = userService.findById(post.getAuthorId());
        return postMapper.toResponseDTO(post, userMapper.toDetailsDTO(user));
    }

    //Buscar post através do id do Usuário.
    public List<PostResponseDTO> findByAuthorId(String authorId) {
        logger.info("Finding Post by author id. ID: {}", authorId);
        var user = userService.findById(authorId);
        var postList = postRepository.findByAuthorId(authorId);
        logger.info("Posts linked to this author found.");
        return postList.stream().map(post -> postMapper.toResponseDTO(post, userMapper.toDetailsDTO(user))).toList();
    }

    //Buscar Post e os comentarios
    public PostWithCommentsDTO listAllComments(String postId) {
        logger.info("List all comments on a post. ID: {}", postId);
        var post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        var user = userService.findById(post.getAuthorId());
        var commentsList = commentService.findByPostId(postId);
        logger.info("The post and its comments were successfully found.");
        return postMapper.toPostCommentsDTO(postMapper.toResponseDTO(post, userMapper.toDetailsDTO(user)), commentsList);
    }

    public PostResponseDTO update(String postId, PostRequestDTO request) {
        logger.info("Update Post. ID: {}", postId);
        var post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        var user = userService.findById(post.getAuthorId());
        postMapper.toUpdateEntity(request, post);
        var updatedPost = postRepository.save(post);
        logger.info("Post successfully updated.");
        return postMapper.toResponseDTO(updatedPost, userMapper.toDetailsDTO(user));
    }

    public void delete(String postId) {
        logger.info("Post Delete. ID: {}", postId);
        var post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        postRepository.delete(post);
        logger.info("Post successfully deleted");
    }
}