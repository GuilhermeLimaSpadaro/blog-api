package com.gspadaro.blogapi.service;


import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.UserPostResponseDTO;
import com.gspadaro.blogapi.dto.UserRequestDTO;
import com.gspadaro.blogapi.dto.UserResponseDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.repository.PostRepository;
import com.gspadaro.blogapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PostRepository postRepo;

    public UserService(UserRepository userRepo, PostRepository postRepo) {
        this.userRepo = userRepo;
        this.postRepo = postRepo;
    }

    public UserResponseDTO create(UserRequestDTO request) {
        User newUser = new User();
        newUser.setName(request.name());
        newUser.setEmail(request.email());
        User savedUser = userRepo.save(newUser);
        return UserResponseDTO.from(savedUser);
    }

    public void delete(String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        userRepo.delete(user);
    }

    public UserResponseDTO update(String id, UserRequestDTO request) {
        User existingUser = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        existingUser.setName(request.name());
        existingUser.setEmail(request.email());
        User updatedUser = userRepo.save(existingUser);
        return UserResponseDTO.from(updatedUser);
    }

    public UserResponseDTO findById(String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return UserResponseDTO.from(user);
    }

    public UserPostResponseDTO findPostsByUserId(String id) {
        List<Post> post = postRepo.findByAuthorId(id);
        return UserPostResponseDTO.from(post);
    }


    public List<UserResponseDTO> findAll() {
        List<User> list = userRepo.findAll();
        return list.stream().map(UserResponseDTO::from).toList();
    }
}