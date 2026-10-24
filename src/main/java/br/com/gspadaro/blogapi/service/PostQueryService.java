package br.com.gspadaro.blogapi.service;

import br.com.gspadaro.blogapi.dto.post.PostWithCommentsDTO;
import br.com.gspadaro.blogapi.exception.ResourceNotFoundException;
import br.com.gspadaro.blogapi.mapper.custom.PostMapper;
import br.com.gspadaro.blogapi.mapper.custom.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PostQueryService {

    private final PostService postService;
    private final CommentService commentService;

    private final PostMapper postMapper;

    private static final Logger logger = LoggerFactory.getLogger(PostQueryService.class);

    public PostQueryService(PostService postService, CommentService commentService, PostMapper postMapper) {
        this.postService = postService;
        this.commentService = commentService;
        this.postMapper = postMapper;
    }

    //Buscar Post e os comentarios
    public PostWithCommentsDTO findWithComments(String postId) {
        logger.info("List all comments on a post. ID: {}", postId);
        var post = postService.findById(postId);
        var commentsList = commentService.findByPostId(postId);
        logger.info("The post and its comments were successfully found.");
        return postMapper.toPostCommentsDTO(post, commentsList);
    }
}
