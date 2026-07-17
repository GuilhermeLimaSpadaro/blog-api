package com.gspadaro.blogapi.dto;

import com.gspadaro.blogapi.domain.User;

public record UserResponseDTO(String id, String name){
    public static UserResponseDTO from(User user){
        return new UserResponseDTO(user.getId(), user.getName());
    }
}
