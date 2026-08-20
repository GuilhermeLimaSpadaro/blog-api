package com.gspadaro.blogapi.controller;

import com.gspadaro.blogapi.dto.UserDetailsDTO;
import com.gspadaro.blogapi.dto.UserRequestDTO;
import com.gspadaro.blogapi.dto.UserResponseDTO;
import com.gspadaro.blogapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping(value = "/users")
@RestController
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserDetailsDTO> create(@RequestBody @Valid UserRequestDTO userRequest) {
        UserDetailsDTO user = service.create(userRequest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.id()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable String id) {
        UserResponseDTO user = service.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable String id, @RequestBody @Valid UserRequestDTO user) {
        return ResponseEntity.ok().body(service.update(id, user));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}