package com.gspadaro.blogapi.service;


import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.*;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.mapper.UserMapper;
import com.gspadaro.blogapi.repository.PostRepository;
import com.gspadaro.blogapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetailsDTO create(UserRequestDTO request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPassword(request.password());
        User savedUser = userRepository.save(user);
        return UserMapper.toDetailsDTO(savedUser);
    }

    public void delete(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        userRepository.delete(user);
    }

    public UserResponseDTO update(String id, UserRequestDTO request) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        existingUser.setName(request.name());
        existingUser.setEmail(request.email());
        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toResponseDTO(updatedUser);
    }

    public UserResponseDTO findById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone());
    }

    public List<UserResponseDTO> findAll() {
        List<User> list = userRepository.findAll();
        return list.stream().map(UserMapper::toResponseDTO).toList();
    }
}