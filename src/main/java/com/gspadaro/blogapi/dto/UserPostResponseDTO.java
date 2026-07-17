package com.gspadaro.blogapi.dto;

import com.gspadaro.blogapi.domain.User;

import java.util.List;

public record UserPostResponseDTO(List<PostResponseDTO> posts) {
    public static UserPostResponseDTO from(User user){
        return new UserPostResponseDTO(user.getPosts().stream().map(PostResponseDTO::from).toList());
    }
}
