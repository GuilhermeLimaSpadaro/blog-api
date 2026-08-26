package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.dto.*;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.mapper.PostMapper;
import com.gspadaro.blogapi.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentService commentService;

    public PostService(PostRepository postRepository, UserService userService, CommentService commentService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentService = commentService;
    }

    public PostResponseDTO create(PostRequestDTO request) {
        Post post = PostMapper.toEntity(request);
        UserDetailsDTO user = userService.findDetailsById(post.getAuthorId());
        Post savedPost = postRepository.save(post);
        return PostMapper.toResponseDTO(savedPost, user);
    }

    public PostResponseDTO findById(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        UserDetailsDTO user = userService.findDetailsById(post.getAuthorId());
        return PostMapper.toResponseDTO(post, user);
    }

    //Buscar post através do id do Usuário.
    public List<PostResponseDTO> findByAuthorId(String authorId) {
        List<Post> postList = postRepository.findByAuthorId(authorId);
        UserDetailsDTO user = userService.findDetailsById(authorId);
        return postList.stream().map(post -> PostMapper.toResponseDTO(post, user)).toList();
    }

    //Buscar Post e os comentarios
    public PostWithCommentsDTO listAllComments(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        UserDetailsDTO user = userService.findDetailsById(post.getAuthorId());
        List<CommentResponseDTO> commentsList = commentService.findAllCommentsByPostId(postId);
        return PostMapper.toPostWithCommentsDTO(PostMapper.toResponseDTO(post, user), commentsList);
    }

    public PostResponseDTO update(String postId, PostRequestDTO request) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        UserDetailsDTO user = userService.findDetailsById(post.getAuthorId());
        PostMapper.updateEntity(post, request);
        Post updatedPost = postRepository.save(post);
        return PostMapper.toResponseDTO(updatedPost, user);
    }

    public void delete(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        postRepository.delete(post);
    }
}