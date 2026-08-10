package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.Comment;
import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.PostRequestDTO;
import com.gspadaro.blogapi.dto.PostResponseDTO;
import com.gspadaro.blogapi.repository.CommentRepository;
import com.gspadaro.blogapi.repository.PostRepository;
import com.gspadaro.blogapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private PostService postService;
    @Captor
    private ArgumentCaptor<Post> captor;

    private User savedUser;
    private Post savedPost;
    private PostRequestDTO requestPost;
    private List<Post> postResponseList;
    private List<Comment> commentList;

    @BeforeEach
    void setUp() {
        savedUser = new User(UUID.randomUUID().toString(), "Guilherme", "guilhermespadaro@gmail.com", "11955447766", "13ABC234");
        savedPost = new Post(UUID.randomUUID().toString(), Instant.now(), "Bom dia!", "Como o dia está lindo hoje!", savedUser);
        Post savedPost01 = new Post(UUID.randomUUID().toString(), Instant.now(), "Partiu viagem", "Vou viajar para São Paulo. Abraços!", savedUser);
        Post savedPost02 = new Post(UUID.randomUUID().toString(), Instant.now(), "Bom dia", "Acordei feliz hoje!", savedUser);
        requestPost = new PostRequestDTO("Bom dia!", "Como o dia está lindo hoje!", savedUser.getId());
        Comment savedComment = new Comment(UUID.randomUUID().toString(), "Muito bom, adorei o post!", savedUser, savedPost);
        Comment savedComment01 = new Comment(UUID.randomUUID().toString(), "Boa viagem mano!", savedUser, savedPost);
        Comment savedComment02 = new Comment(UUID.randomUUID().toString(), "Aproveite!", savedUser, savedPost);

        postResponseList = List.of(savedPost, savedPost01, savedPost02);
        commentList = List.of(savedComment, savedComment01, savedComment02);
    }

    @Test
    @DisplayName("Should create a post successfully.")
    void shouldCreatePost() {
        //Arrange
        doReturn(Optional.of(savedUser)).when(userRepository).findById(requestPost.authorId());
        doReturn(savedPost).when(postRepository).save(any(Post.class));
        //Act
        var result = postService.create(requestPost);
        //Assert
        verify(postRepository).save(captor.capture());
        var postCaptor = captor.getValue();
        assertNotNull(postCaptor);
        assertEquals(requestPost.authorId(), postCaptor.getAuthor().getId());
        assertEquals(requestPost.title(), postCaptor.getTitle());
        assertEquals(requestPost.body(), postCaptor.getBody());
        assertEquals(savedPost.getId(), result.id());
        assertEquals(savedPost.getDate(), result.date());
        assertEquals(savedPost.getTitle(), result.title());
        assertEquals(savedPost.getBody(), result.body());
        assertEquals(savedPost.getAuthor().getId(), result.author().id());
    }

    @Test
    @DisplayName("Should delete post successfully")
    void shouldDeletePost() {
        //Arrange
        doReturn(Optional.of(savedPost)).when(postRepository).findById(savedPost.getId());
        doNothing().when(postRepository).delete(savedPost);
        //Act
        postService.delete(savedPost.getId());
        //Assert
        verify(postRepository).delete(savedPost);
    }

    @Test
    @DisplayName("Should update post")
    void shouldUpdatePost() {
        //Arrange
        doReturn(Optional.of(savedPost)).when(postRepository).findById(savedPost.getId());
        doReturn(savedPost).when(postRepository).save(any(Post.class));
        //Act
        var result = postService.update(savedPost.getId(), requestPost);
        //Assert
        verify(postRepository).findById(savedPost.getId());
        verify(postRepository).save(captor.capture());
        var postCaptor = captor.getValue();
        assertNotNull(postCaptor);
        assertEquals(savedPost.getId(), result.id());
        assertEquals(savedPost.getDate(), result.date());
        assertEquals(savedPost.getTitle(), result.title());
        assertEquals(savedPost.getBody(), result.body());
        assertEquals(savedPost.getAuthor().getId(), result.author().id());
        assertEquals(requestPost.title(), postCaptor.getTitle());
        assertEquals(requestPost.body(), postCaptor.getBody());
        assertEquals(requestPost.authorId(), postCaptor.getAuthor().getId());
    }

    @Test
    @DisplayName("Should find post by id successfully")
    void shouldFindPostById() {
        //Arrange
        doReturn(Optional.of(savedPost)).when(postRepository).findById(savedPost.getId());
        //Act
        var result = postService.findById(savedPost.getId());
        //Assert
        verify(postRepository).findById(savedPost.getId());
        assertEquals(savedPost.getId(), result.id());
        assertEquals(savedPost.getDate(), result.date());
        assertEquals(savedPost.getTitle(), result.title());
        assertEquals(savedPost.getBody(), result.body());
        assertEquals(savedPost.getAuthor().getId(), result.author().id());
    }

    @Test
    @DisplayName("Should find post by user id")
    void shouldFindPostByUserId() {
        //Arrange
        doReturn(postResponseList).when(postRepository).findByAuthorId(savedUser.getId());
        //Act
        var result = postService.findByAuthorId(savedUser.getId());
        //Assert
        verify(postRepository).findByAuthorId(savedUser.getId());
        assertNotNull(result);
        Optional<Post> postFound = postResponseList.stream().filter(post -> savedUser.getId().equals(post.getAuthor().getId())).findFirst();
        Optional<PostResponseDTO> returnPost = result.stream().filter(post -> savedUser.getId().equals(post.author().id())).findFirst();
        assertTrue(postFound.isPresent());
        assertTrue(returnPost.isPresent());
        assertEquals(postFound.get().getId(), returnPost.get().id());
    }

    @Test
    @DisplayName("Should find post with comments")
    void shouldListPostWithComment() {
        //Arrange
        doReturn(Optional.of(savedPost)).when(postRepository).findById(savedPost.getId());
        doReturn(commentList).when(commentRepository).findByPostId(savedPost.getId());
        //Act
        var result = postService.listAllComments(savedPost.getId());
        //Assert
        verify(postRepository).findById(savedPost.getId());
        verify(commentRepository).findByPostId(savedPost.getId());
        assertNotNull(result);
        assertEquals(savedPost.getId(), result.post().id());
        assertEquals(savedPost.getDate(), result.post().date());
        assertEquals(savedPost.getTitle(), result.post().title());
        assertEquals(savedPost.getBody(), result.post().body());
        assertEquals(savedPost.getAuthor().getId(), result.post().author().id());
        var commentsFound = commentList.stream().filter(comment -> savedPost.getId().equals(comment.getPost().getId())).findFirst();
        var returnComments = result.comment().stream().filter(commentResponseDTO -> savedPost.getId().equals(commentResponseDTO.postId())).findFirst();
        assertTrue(commentsFound.isPresent());
        assertTrue(returnComments.isPresent());
        assertEquals(commentsFound.get().getId(), returnComments.get().id());
        assertEquals(commentsFound.get().getText(), returnComments.get().text());
        assertEquals(commentsFound.get().getDate(), returnComments.get().date());
        assertEquals(commentsFound.get().getAuthor().getId(), returnComments.get().author().id());
        assertEquals(commentsFound.get().getPost().getAuthor().getId(), returnComments.get().author().id());
    }
}