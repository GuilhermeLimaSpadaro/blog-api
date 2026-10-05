package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.dto.comment.CommentResponseDTO;
import com.gspadaro.blogapi.dto.post.PostRequestDTO;
import com.gspadaro.blogapi.dto.post.PostResponseDTO;
import com.gspadaro.blogapi.dto.post.PostWithCommentsDTO;
import com.gspadaro.blogapi.dto.user.UserDetailsDTO;
import com.gspadaro.blogapi.model.Post;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.mapper.custom.PostMapper;
import com.gspadaro.blogapi.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserService userService;
    private final CommentService commentService;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    public PostService(PostRepository postRepository, PostMapper postMapper, UserService userService, CommentService commentService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userService = userService;
        this.commentService = commentService;
    }

    public PostResponseDTO create(PostRequestDTO request) {
        var post = postMapper.toEntity(request);
        var user = userService.findById(post.getAuthorId());
        var savedPost = postRepository.save(post);
        return postMapper.toResponseDTO(savedPost, user);
    }

    public PostResponseDTO findById(String postId) {
        var post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        var user = userService.findById(post.getAuthorId());
        return postMapper.toResponseDTO(post, user);
    }

    //Buscar post através do id do Usuário.
    public List<PostResponseDTO> findByAuthorId(String authorId) {
        var postList = postRepository.findByAuthorId(authorId);
        var user = userService.findById(authorId);
        return postList.stream().map(post -> postMapper.toResponseDTO(post, user)).toList();
    }

    //Buscar Post e os comentarios
    public PostWithCommentsDTO listAllComments(String postId) {
        var post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        var user = userService.findById(post.getAuthorId());
        var commentsList = commentService.findAllCommentsByPostId(postId);
        return postMapper.toPostCommentsDTO(postMapper.toResponseDTO(post, user), commentsList);
    }

    public PostResponseDTO update(String postId, PostRequestDTO request) {
        var post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        var user = userService.findById(post.getAuthorId());
        postMapper.toUpdateEntity(request, post);
        var updatedPost = postRepository.save(post);
        return postMapper.toResponseDTO(updatedPost, user);
    }

    public void delete(String postId) {
        var post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        postRepository.delete(post);
    }
}