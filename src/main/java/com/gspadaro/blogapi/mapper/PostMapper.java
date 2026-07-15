package com.gspadaro.blogapi.mapper;

import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.CommentResponseDTO;
import com.gspadaro.blogapi.dto.PostRequestDTO;
import com.gspadaro.blogapi.dto.PostResponseDTO;
import com.gspadaro.blogapi.dto.PostWithCommentsDTO;

import java.util.List;

public class PostMapper {

    public static Post toEntity(PostRequestDTO request, User user) {
        return new Post(request.title(), request.body(), user);
    }

    public static void updateEntity(Post post, PostRequestDTO request) {
        post.setTitle(request.title());
        post.setBody(request.body());
    }

    public static PostResponseDTO toResponseDTO(Post post) {
        return new PostResponseDTO(post.getId(), post.getDate(), post.getTitle(), post.getBody(), UserMapper.toDetailsDTO(post.getAuthor()));
    }

    public static PostWithCommentsDTO toPostWithCommentsDTO(PostResponseDTO post, List<CommentResponseDTO> commentList) {
        return new PostWithCommentsDTO(post, commentList);
    }


    public static List<PostResponseDTO> toList(List<Post> postList) {
        return postList.stream().map(PostMapper::toResponseDTO).toList();
    }
}
