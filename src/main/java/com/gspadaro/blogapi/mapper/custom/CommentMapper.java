package com.gspadaro.blogapi.mapper.custom;

import com.gspadaro.blogapi.dto.comment.CommentRequestDTO;
import com.gspadaro.blogapi.dto.comment.CommentResponseDTO;
import com.gspadaro.blogapi.mapper.ObjectMapper;
import com.gspadaro.blogapi.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper extends ObjectMapper<Comment, CommentRequestDTO, CommentResponseDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Override
    Comment toEntity(CommentRequestDTO request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Override
    void toUpdateEntity(CommentRequestDTO request, @MappingTarget Comment entity);
}
