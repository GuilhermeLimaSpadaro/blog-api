package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.Comment;
import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.PostRequestDTO;
import com.gspadaro.blogapi.dto.PostResponseDTO;
import com.gspadaro.blogapi.dto.PostWithCommentsDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.mapper.CommentMapper;
import com.gspadaro.blogapi.mapper.PostMapper;
import com.gspadaro.blogapi.repository.CommentRepository;
import com.gspadaro.blogapi.repository.PostRepository;
import com.gspadaro.blogapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public PostResponseDTO create(PostRequestDTO request) {
        User user = userRepository.findById(request.authorId()).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        Post post = PostMapper.toEntity(request, user);
        Post savedPost = postRepository.save(post);
        return PostMapper.toResponseDTO(savedPost);
    }

    public void delete(String id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        postRepository.delete(post);
    }

    public PostResponseDTO update(String id, PostRequestDTO request) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        PostMapper.updateEntity(post, request);
        Post updatedPost = postRepository.save(post);
        return PostMapper.toResponseDTO(updatedPost);
    }

    public PostResponseDTO findById(String id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return PostMapper.toResponseDTO(post);
    }

    //Buscar post através do id do Usuário.
    public List<PostResponseDTO> findByUserId(String id) {
        List<Post> postList = postRepository.findByAuthorId(id);
        return postList.stream().map(PostMapper::toResponseDTO).toList();
    }

    //Buscar Comentarios de um Post
    public PostWithCommentsDTO listAllComments(String id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        List<Comment> commentList = commentRepository.findByPostId(id);
        return PostMapper.toPostWithCommentsDTO(PostMapper.toResponseDTO(post), CommentMapper.toList(commentList));
    }
}