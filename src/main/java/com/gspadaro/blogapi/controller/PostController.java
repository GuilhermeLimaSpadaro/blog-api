package com.gspadaro.blogapi.controller;

import com.gspadaro.blogapi.dto.PostRequestDTO;
import com.gspadaro.blogapi.dto.PostResponseDTO;
import com.gspadaro.blogapi.dto.PostWithCommentsDTO;
import com.gspadaro.blogapi.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping(value = "/posts")
@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> create(@RequestBody PostRequestDTO postRequest) {
        PostResponseDTO post = postService.create(postRequest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.id()).toUri();
        return ResponseEntity.created(uri).body(post);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PostResponseDTO> update(@PathVariable String id, @RequestBody PostRequestDTO request) {
        return ResponseEntity.ok().body(postService.update(id, request));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok().body(postService.findById(id));
    }

    @GetMapping("users/{id}")
    public ResponseEntity<List<PostResponseDTO>> findByUserId(@PathVariable String id) {
        List<PostResponseDTO> posts = postService.findByUserId(id);
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<PostWithCommentsDTO> findAllPostsWithComments(@PathVariable String id) {
        return ResponseEntity.ok().body(postService.listAllComments(id));
    }
}