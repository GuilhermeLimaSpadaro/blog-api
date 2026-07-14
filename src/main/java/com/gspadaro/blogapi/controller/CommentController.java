package com.gspadaro.blogapi.controller;

import com.gspadaro.blogapi.dto.CommentRequestDTO;
import com.gspadaro.blogapi.dto.CommentResponseDTO;
import com.gspadaro.blogapi.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping(value = "/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> create(@RequestBody CommentRequestDTO request) {
        CommentResponseDTO comment = commentService.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comment.id()).toUri();
        return ResponseEntity.created(uri).body(comment);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CommentResponseDTO> update(@PathVariable String id, @RequestBody CommentRequestDTO request) {
        CommentResponseDTO comment = commentService.update(id, request);
        return ResponseEntity.ok().body(comment);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CommentResponseDTO> delete(@PathVariable String id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> listAll() {
        return ResponseEntity.ok().body(commentService.findAll());
    }
}
