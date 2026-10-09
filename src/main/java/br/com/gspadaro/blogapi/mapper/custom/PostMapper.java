package br.com.gspadaro.blogapi.mapper.custom;

import br.com.gspadaro.blogapi.dto.comment.CommentResponseDTO;
import br.com.gspadaro.blogapi.dto.post.PostRequestDTO;
import br.com.gspadaro.blogapi.dto.post.PostResponseDTO;
import br.com.gspadaro.blogapi.dto.post.PostWithCommentsDTO;
import br.com.gspadaro.blogapi.dto.user.UserDetailsDTO;
import br.com.gspadaro.blogapi.mapper.ObjectMapper;
import br.com.gspadaro.blogapi.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper extends ObjectMapper<Post, PostRequestDTO, PostResponseDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Override
    Post toEntity(PostRequestDTO post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Override
    void toUpdateEntity(PostRequestDTO request, @MappingTarget Post entity);

    @Mapping(target = "user", ignore = true)
    @Override
    PostResponseDTO toResponseDTO(Post post);

    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "user", source = "user")
    PostResponseDTO toResponseDTO(Post post, UserDetailsDTO user);

    @Mapping(target = "comments", source = "commentList")
    PostWithCommentsDTO toPostCommentsDTO(PostResponseDTO post, List<CommentResponseDTO> commentList);


}
