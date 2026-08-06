package com.gspadaro.blogapi.mapper;

import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.UserDetailsDTO;
import com.gspadaro.blogapi.dto.UserRequestDTO;
import com.gspadaro.blogapi.dto.UserResponseDTO;

import java.util.List;

public class UserMapper {

    public static User toEntity(UserRequestDTO request) {
        return new User(request.name(), request.name(), request.email(), request.phone(), request.password());
    }

    public static UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone());
    }

    public static UserDetailsDTO toDetailsDTO(User user) {
        return new UserDetailsDTO(user.getId(), user.getName());
    }

    public static List<UserResponseDTO> toList(List<User> userList) {
        return userList.stream().map(UserMapper::toResponseDTO).toList();
    }
}
