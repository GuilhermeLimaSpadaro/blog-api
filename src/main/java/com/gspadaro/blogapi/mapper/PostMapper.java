package com.gspadaro.blogapi.mapper;

import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.CommentResponseDTO;
import com.gspadaro.blogapi.dto.PostRequestDTO;
import com.gspadaro.blogapi.dto.PostResponseDTO;
import com.gspadaro.blogapi.dto.UserDetailsDTO;

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
        return new PostResponseDTO(post.getId()
                , post.getDate()
                , post.getTitle()
                , post.getBody()
                , new UserDetailsDTO(post.getAuthor().getId()
                , post.getAuthor().getName())
                , post.getComments().stream().map(comment ->
                new CommentResponseDTO(comment.getId(), comment.getText()
                        , comment.getDate()
                        , new UserDetailsDTO(comment.getAuthor().getId()
                        , comment.getAuthor().getName()))).toList());
    }

    public static List<PostResponseDTO> toList(List<Post> postList) {
        return postList.stream().map(PostMapper::toResponseDTO).toList();
    }
}
