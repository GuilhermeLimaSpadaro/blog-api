package com.gspadaro.blogapi.dto;

import com.gspadaro.blogapi.domain.Post;

import java.util.List;

public record UserPostResponseDTO(List<PostResponseDTO> posts) {
    public static UserPostResponseDTO from(List<Post> posts) {
        return new UserPostResponseDTO(posts.stream().map(PostResponseDTO::from).toList());
    }
}
