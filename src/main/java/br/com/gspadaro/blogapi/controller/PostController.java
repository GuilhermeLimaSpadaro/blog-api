package br.com.gspadaro.blogapi.controller;

import br.com.gspadaro.blogapi.dto.post.PostRequestDTO;
import br.com.gspadaro.blogapi.dto.post.PostResponseDTO;
import br.com.gspadaro.blogapi.dto.post.PostWithCommentsDTO;
import br.com.gspadaro.blogapi.service.PostQueryService;
import br.com.gspadaro.blogapi.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping(value = "/api/v1/posts")
@RestController
public class PostController {

    private final PostQueryService queryService;
    private final PostService postService;

    public PostController(PostQueryService queryService, PostService postService) {
        this.queryService = queryService;
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> create(@RequestBody @Valid PostRequestDTO postRequest) {
        PostResponseDTO post = postService.create(postRequest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.id()).toUri();
        return ResponseEntity.created(uri).body(post);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok().body(postService.findById(id));
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<List<PostResponseDTO>> findByUserId(@PathVariable String id) {
        List<PostResponseDTO> posts = postService.findByUserId(id);
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping(value = "/{id}/comments")
    public ResponseEntity<PostWithCommentsDTO> findWithComments(@PathVariable String id) {
        return ResponseEntity.ok().body(queryService.findWithComments(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PostResponseDTO> update(@PathVariable String id, @RequestBody @Valid PostRequestDTO request) {
        return ResponseEntity.ok().body(postService.update(id, request));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}