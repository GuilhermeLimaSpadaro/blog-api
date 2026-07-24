package com.gspadaro.blogapi.dto;

import com.gspadaro.blogapi.domain.Post;

import java.time.LocalDate;
import java.util.List;

public record PostResponseDTO(String id, LocalDate date, String title, String body, UserResponseDTO author, List<CommentResponseDTO> comments) {

    public static PostResponseDTO from(Post post) {
        return new PostResponseDTO(post.getId(), post.getDate(), post.getTitle(), post.getBody(), UserResponseDTO.from(post.getAuthor
                ()), post.getComments().stream().map(CommentResponseDTO::from).toList());
    }

    public static List<PostResponseDTO> from(List<Post> posts) {
        return posts.stream().map(PostResponseDTO::from).toList();
    }
}
