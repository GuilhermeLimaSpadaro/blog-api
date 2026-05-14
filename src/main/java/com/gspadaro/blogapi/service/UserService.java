package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.UserDTO;
import com.gspadaro.blogapi.exception.ObjectNotFoundException;
import com.gspadaro.blogapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserDTO create(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        User savedUser = repository.save(user);
        return UserDTO.from(savedUser);
    }

    public UserDTO findById(String id) {
        User user = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Resource not found"));
        return UserDTO.from(user);
    }

    public List<UserDTO> findAll() {
        List<User> list = repository.findAll();
        return list.stream().map(user -> UserDTO.from(user)).toList();
    }
}