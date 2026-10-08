package com.gspadaro.blogapi.mapper.custom;

import com.gspadaro.blogapi.dto.comment.CommentResponseDTO;
import com.gspadaro.blogapi.dto.post.PostRequestDTO;
import com.gspadaro.blogapi.dto.post.PostResponseDTO;
import com.gspadaro.blogapi.dto.post.PostWithCommentsDTO;
import com.gspadaro.blogapi.dto.user.UserDetailsDTO;
import com.gspadaro.blogapi.mapper.ObjectMapper;
import com.gspadaro.blogapi.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper extends ObjectMapper<Post, PostRequestDTO, PostResponseDTO> {

    @Mapping(target = "comments", source = "commentList")
    PostWithCommentsDTO toPostCommentsDTO(PostResponseDTO post, List<CommentResponseDTO> commentList);

    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "user", source = "user")
    PostResponseDTO toResponseDTO(Post post, UserDetailsDTO user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Override
    Post toEntity(PostRequestDTO post);

    @Mapping(target = "user", ignore = true)
    @Override
    PostResponseDTO toResponseDTO(Post post);
}
