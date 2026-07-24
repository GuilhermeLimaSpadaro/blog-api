package com.gspadaro.blogapi.controller;

import com.gspadaro.blogapi.dto.PostRequestDTO;
import com.gspadaro.blogapi.dto.PostResponseDTO;
import com.gspadaro.blogapi.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping(value = "/posts")
@RestController
public class PostController {

    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> create(@RequestBody PostRequestDTO postRequest) {
        PostResponseDTO userCreated = service.create(postRequest);
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

    @GetMapping(value = "/{title}")
    public ResponseEntity<List<PostResponseDTO>> findPostByTitle(@PathVariable String title){
        return ResponseEntity.ok().body(service.findPostByTitle(title));
    }
}