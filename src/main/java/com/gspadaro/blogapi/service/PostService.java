package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.PostRequestDTO;
import com.gspadaro.blogapi.dto.PostResponseDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.mapper.PostMapper;
import com.gspadaro.blogapi.repository.PostRepository;
import com.gspadaro.blogapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

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
        return PostMapper.toResponseDTO(savedPost);
    }

    public void delete(String id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        postRepository.delete(post);
    }

    public PostResponseDTO update(String id, PostRequestDTO request) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        post.setDate(request.date());
        post.setTitle(request.title());
        post.setBody(request.body());
        Post updatedPost = postRepository.save(post);
        return PostMapper.toResponseDTO(updatedPost);
    }

    public PostResponseDTO findById(String id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return PostMapper.toResponseDTO(post);
    }

    public List<PostResponseDTO> findPostsByUserId(String id) {
        List<Post> postList = postRepository.findByAuthorId(id);
        return postList.stream().map(PostMapper::toResponseDTO).toList();
    }

    public List<PostResponseDTO> findPostByTitle(String title) {
        List<Post> postList = postRepository.findByTitleContainingIgnoreCase(title);
        return PostMapper.toList(postList);
    }

    public List<PostResponseDTO> findAll() {
        List<Post> postList = postRepository.findAll();
        return PostMapper.toList(postList);
    }
}
