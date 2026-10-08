package com.gspadaro.blogapi.service;

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

    public UserResponseDTO create(UserRequestDTO request) {
        logger.info("Create User.");
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        logger.info("User successfully created. ID: {}", savedUser.getId());
        return userMapper.toResponseDTO(savedUser);
    }

    public UserResponseDTO findById(String userId) {
        logger.info("Finding User. ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            logger.warn("User not found. ID: {}", userId);
            return new ResourceNotFoundException("User not found");
        });
        logger.info("User successfully found.");
        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO update(String userId, UserRequestDTO request) {
        logger.info("Update User. ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userMapper.toUpdateEntity(request, user);
        User updatedUser = userRepository.save(user);
        logger.info("User successfully updated");
        return userMapper.toResponseDTO(updatedUser);
    }

    public void delete(String userId) {
        logger.info("User Delete. ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
        logger.info("User successfully deleted.");
    }
}