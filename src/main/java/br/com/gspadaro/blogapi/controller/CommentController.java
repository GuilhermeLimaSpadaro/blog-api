package br.com.gspadaro.blogapi.controller;

import br.com.gspadaro.blogapi.dto.comment.CommentRequestDTO;
import br.com.gspadaro.blogapi.dto.comment.CommentResponseDTO;
import br.com.gspadaro.blogapi.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping(value = "/api/v1/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> create(@RequestBody @Valid CommentRequestDTO request) {
        CommentResponseDTO comment = commentService.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comment.id()).toUri();
        return ResponseEntity.created(uri).body(comment);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommentResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok().body(commentService.findById(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CommentResponseDTO> update(@PathVariable String id, @RequestBody @Valid CommentRequestDTO request) {
        CommentResponseDTO comment = commentService.update(id, request);
        return ResponseEntity.ok().body(comment);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
