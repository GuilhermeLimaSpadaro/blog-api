package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.SimpleResponseDTO;
import com.gspadaro.blogapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<SimpleResponseDTO> findAll() {
        List<User> list = repository.findAll();
        return list.stream().map(user -> new SimpleResponseDTO(user.getName(), user.getEmail())).toList();
    }
}