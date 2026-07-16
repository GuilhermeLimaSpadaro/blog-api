package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.UserDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserDTO create(UserDTO request) {
        User newUser = new User();
        newUser.setName(request.name());
        newUser.setEmail(request.email());
        User savedUser = repository.save(newUser);
        return UserDTO.from(savedUser);
    }

    public void delete(String id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Object not found"));
        repository.delete(user);
    }

    public UserDTO update(String id, UserDTO request) {
        User existingUser = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Object not found"));
        existingUser.setName(request.name());
        existingUser.setEmail(request.email());
        User updatedUser = repository.save(existingUser);
        return UserDTO.from(updatedUser);
    }

    public UserDTO findById(String id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Object not found"));
        return UserDTO.from(user);
    }

    public List<UserDTO> findAll() {
        List<User> list = repository.findAll();
        return list.stream().map(user -> UserDTO.from(user)).toList();
    }
}