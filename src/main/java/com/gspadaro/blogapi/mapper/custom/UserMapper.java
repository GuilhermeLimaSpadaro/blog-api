package com.gspadaro.blogapi.mapper.custom;

import com.gspadaro.blogapi.dto.user.UserDetailsDTO;
import com.gspadaro.blogapi.dto.user.UserRequestDTO;
import com.gspadaro.blogapi.dto.user.UserResponseDTO;
import com.gspadaro.blogapi.mapper.ObjectMapper;
import com.gspadaro.blogapi.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper extends ObjectMapper<User, UserRequestDTO, UserResponseDTO> {

    UserDetailsDTO toDetailsDTO(UserResponseDTO user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDTO user);

    @Mapping(target = "id", ignore = true)
    @Override
    void toUpdateEntity(UserRequestDTO request, @MappingTarget User entity);
}
