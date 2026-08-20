package com.gspadaro.blogapi.mapper;

import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.dto.*;

import java.util.List;

public class PostMapper {

    public static Post toEntity(PostRequestDTO request) {
        return new Post(request.title(), request.body(), request.authorId());
    }

    public static void updateEntity(Post post, PostRequestDTO request) {
        post.setTitle(request.title());
        post.setBody(request.body());
    }

    public static PostResponseDTO toResponseDTO(Post post, UserDetailsDTO user) {
        return new PostResponseDTO(post.getId(), post.getDate(), post.getTitle(), post.getBody(), user);
    }

    public static PostWithCommentsDTO toPostWithCommentsDTO(PostResponseDTO post, List<CommentResponseDTO> commentList) {
        return new PostWithCommentsDTO(post, commentList);
    }
}
