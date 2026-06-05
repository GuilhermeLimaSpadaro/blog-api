package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.UserPostResponseDTO;
import com.gspadaro.blogapi.dto.UserRequestDTO;
import com.gspadaro.blogapi.dto.UserResponseDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponseDTO create(UserRequestDTO request) {
        User newUser = new User();
        newUser.setName(request.name());
        newUser.setEmail(request.email());
        User savedUser = repository.save(newUser);
        return UserResponseDTO.from(savedUser);
    }

    public void delete(String id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        repository.delete(user);
    }

    public UserResponseDTO update(String id, UserRequestDTO request) {
        User existingUser = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        existingUser.setName(request.name());
        existingUser.setEmail(request.email());
        User updatedUser = repository.save(existingUser);
        return UserResponseDTO.from(updatedUser);
    }

    public UserResponseDTO findById(String id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return UserResponseDTO.from(user);
    }

    public UserPostResponseDTO findPosts(String id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return UserPostResponseDTO.from(user);
    }

    public List<UserResponseDTO> findAll() {
        List<User> list = repository.findAll();
        return list.stream().map(UserResponseDTO::from).toList();
    }
}