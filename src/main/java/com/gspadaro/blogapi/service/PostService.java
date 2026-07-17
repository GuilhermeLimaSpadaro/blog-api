package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.dto.PostRequestDTO;
import com.gspadaro.blogapi.dto.PostResponseDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public PostResponseDTO update(String id, PostRequestDTO request) {
        Post post = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        post.setTitle(request.title());
        post.setBody(request.body());
        repository.save(post);
        return PostResponseDTO.from(post);
    }

    public PostResponseDTO findById(String id) {
        Post post = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return PostResponseDTO.from(post);
    }

    public List<PostResponseDTO> findAll() {
        List<Post> posts = repository.findAll();
        return posts.stream().map(PostResponseDTO::from).toList();
    }
}
