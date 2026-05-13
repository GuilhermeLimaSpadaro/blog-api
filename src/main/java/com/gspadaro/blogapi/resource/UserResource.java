package com.gspadaro.blogapi.resource;

import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.UserDTO;
import com.gspadaro.blogapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = "/users")
@RestController
public class UserResource {

    private UserService service;

    public UserResource(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable String id){
        UserDTO user = service.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }
}