package com.gspadaro.blogapi.dto;

import com.gspadaro.blogapi.domain.User;

public record AuthorDTO(String id, String name) {
    public static AuthorDTO from(User user) {
        return new AuthorDTO(user.getId(), user.getName());
    }
}
