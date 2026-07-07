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

    public UserDTO findById (String id){
        User user = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Resource not found"));
        return new UserDTO(user.getName(), user.getEmail());
    }

    public List<UserDTO> findAll() {
        List<User> list = repository.findAll();
        return list.stream().map(user -> new UserDTO(user.getName(), user.getEmail())).toList();
    }
}