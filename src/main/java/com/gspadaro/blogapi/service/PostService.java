package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.PostRequestDTO;
import com.gspadaro.blogapi.dto.PostResponseDTO;
import com.gspadaro.blogapi.dto.UserRequestDTO;
import com.gspadaro.blogapi.dto.UserResponseDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.repository.PostRepository;
import com.gspadaro.blogapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public PostResponseDTO create(PostRequestDTO request) {
        Post newPost = new Post();
        User author = userRepository.findById(request.authorId()).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        newPost.setDate(request.date());
        newPost.setTitle(request.title());
        newPost.setBody(request.body());
        newPost.setAuthor(author);
        Post savedPost = postRepository.save(newPost);
        return PostResponseDTO.from(savedPost);
    }

    public void delete(String id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        postRepository.delete(post);
    }

    public PostResponseDTO update(String id, PostRequestDTO request) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        post.setTitle(request.title());
        post.setBody(request.body());
        postRepository.save(post);
        return PostResponseDTO.from(post);
    }

    public PostResponseDTO findById(String id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return PostResponseDTO.from(post);
    }

    public List<PostResponseDTO> findAll() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(PostResponseDTO::from).toList();
    }
}
