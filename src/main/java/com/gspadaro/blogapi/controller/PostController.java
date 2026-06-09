package com.gspadaro.blogapi.controller;

import com.gspadaro.blogapi.dto.PostRequestDTO;
import com.gspadaro.blogapi.dto.PostResponseDTO;
import com.gspadaro.blogapi.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/posts")
@RestController
public class PostController {

    private PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PostResponseDTO> update(@PathVariable String id, @RequestBody PostRequestDTO request) {
        return ResponseEntity.ok().body(service.update(id, request));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }
}
