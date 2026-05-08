package com.gspadaro.blogapi.services;

import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAll() {
        return repository.findAll();
    }
}