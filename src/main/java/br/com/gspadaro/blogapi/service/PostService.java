package br.com.gspadaro.blogapi.service;

import br.com.gspadaro.blogapi.dto.post.PostRequestDTO;
import br.com.gspadaro.blogapi.dto.post.PostResponseDTO;
import br.com.gspadaro.blogapi.dto.post.PostWithCommentsDTO;
import br.com.gspadaro.blogapi.exception.ResourceNotFoundException;
import br.com.gspadaro.blogapi.mapper.custom.PostMapper;
import br.com.gspadaro.blogapi.mapper.custom.UserMapper;
import br.com.gspadaro.blogapi.repository.CommentRepository;
import br.com.gspadaro.blogapi.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final UserService userService;

    private final PostRepository postRepository;

    private final PostMapper postMapper;
    private final UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    public PostService(PostRepository postRepository, PostMapper postMapper, UserMapper userMapper, UserService userService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    public PostResponseDTO create(PostRequestDTO request) {
        logger.info("Create Post");
        var post = postMapper.toEntity(request);
        var user = userService.findById(post.getUserId());
        var savedPost = postRepository.save(post);
        logger.info("Post successfully created. PostID: {}, AuthorID: {}", post.getId(), user.id());
        return postMapper.toResponseDTO(savedPost, userMapper.toDetailsDTO(user));
    }

    public PostResponseDTO findById(String postId) {
        logger.info("Finding Post. ID: {}", postId);
        var post = postRepository.findById(postId).orElseThrow(() -> {
            logger.warn("Post not found. ID: {}", postId);
            return new ResourceNotFoundException("Post not found");
        });
        logger.info("Post successfully found.");
        var user = userService.findById(post.getUserId());
        return postMapper.toResponseDTO(post, userMapper.toDetailsDTO(user));
    }

    //Buscar post através do id do Usuário.
    public List<PostResponseDTO> findByUserId(String userId) {
        logger.info("Finding Post by user id. ID: {}", userId);
        var user = userService.findById(userId);
        var postList = postRepository.findByUserId(user.id());
        logger.info("Post successfully found with user id.");
        return postList.stream().map(post -> postMapper.toResponseDTO(post, userMapper.toDetailsDTO(user))).toList();
    }

    public PostResponseDTO update(String postId, PostRequestDTO request) {
        logger.info("Update Post. ID: {}", postId);
        var post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        var user = userService.findById(post.getUserId());
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