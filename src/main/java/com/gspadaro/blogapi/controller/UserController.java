package com.gspadaro.blogapi.controller;

import com.gspadaro.blogapi.dto.PostResponseDTO;
import com.gspadaro.blogapi.dto.UserRequestDTO;
import com.gspadaro.blogapi.dto.UserResponseDTO;
import com.gspadaro.blogapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping(value = "/users")
@RestController
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO userRequest) {
        UserResponseDTO userCreated = service.create(userRequest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(userCreated.id()).toUri();
        return ResponseEntity.created(uri).body(userCreated);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable String id, @RequestBody UserRequestDTO user) {
        return ResponseEntity.ok().body(service.update(id, user));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable String id) {
        UserResponseDTO user = service.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<PostResponseDTO>> findPostsByUserId(@PathVariable String id) {
        List<PostResponseDTO> posts = service.findPostsByUserId(id);
        return ResponseEntity.ok().body(posts);
    }
}