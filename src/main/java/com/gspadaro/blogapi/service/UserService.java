package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.dto.user.UserDetailsDTO;
import com.gspadaro.blogapi.dto.user.UserRequestDTO;
import com.gspadaro.blogapi.dto.user.UserResponseDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.mapper.custom.UserMapper;
import com.gspadaro.blogapi.model.User;
import com.gspadaro.blogapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDetailsDTO create(UserRequestDTO request) {
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.toDetailsDTO(savedUser);
    }

    public UserResponseDTO findById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO update(String userId, UserRequestDTO request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        userMapper.toUpdateEntity(request, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDTO(updatedUser);
    }

    public void delete(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        userRepository.delete(user);
    }
}