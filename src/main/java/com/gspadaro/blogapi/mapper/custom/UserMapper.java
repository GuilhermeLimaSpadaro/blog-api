package com.gspadaro.blogapi.mapper.custom;

import com.gspadaro.blogapi.dto.user.UserDetailsDTO;
import com.gspadaro.blogapi.dto.user.UserRequestDTO;
import com.gspadaro.blogapi.dto.user.UserResponseDTO;
import com.gspadaro.blogapi.mapper.ObjectMapper;
import com.gspadaro.blogapi.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends ObjectMapper<User, UserRequestDTO, UserResponseDTO> {
    UserDetailsDTO toDetailsDTO(User entity);
}
