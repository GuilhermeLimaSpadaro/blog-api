package com.gspadaro.blogapi.mapper.custom;

import com.gspadaro.blogapi.dto.comment.CommentRequestDTO;
import com.gspadaro.blogapi.dto.comment.CommentResponseDTO;
import com.gspadaro.blogapi.mapper.ObjectMapper;
import com.gspadaro.blogapi.model.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper extends ObjectMapper<Comment, CommentRequestDTO, CommentResponseDTO> {
}
