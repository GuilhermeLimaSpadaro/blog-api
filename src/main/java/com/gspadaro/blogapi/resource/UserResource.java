package com.gspadaro.blogapi.resource;

import com.gspadaro.blogapi.dto.SimpleResponseDTO;
import com.gspadaro.blogapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public ResponseEntity<List<SimpleResponseDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }
}