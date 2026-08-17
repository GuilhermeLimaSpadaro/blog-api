package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.Comment;
import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.PostRequestDTO;
import com.gspadaro.blogapi.dto.PostResponseDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
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
    private PostRequestDTO postRequest;

    @BeforeEach
    void setUp() {
        savedUser = new User(UUID.randomUUID().toString(), "Guilherme", "guilhermespadaro@gmail.com", "11955447766", "13ABC234");
        savedPost = new Post(UUID.randomUUID().toString(), Instant.now(), "Bom dia!", "Como o dia esta lindo hoje!", savedUser);
        postRequest = new PostRequestDTO("Bom tarde!", "Vamos tomar um cafe?!", savedUser.getId());
    }

    @Test
    @DisplayName("Should create a post successfully.")
    void shouldCreatePost() {
        //Arrange
        when(userRepository.findById(postRequest.authorId())).thenReturn(Optional.of(savedUser));
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
        //Act
        var result = postService.create(postRequest);
        //Assert
        verify(userRepository).findById(postRequest.authorId());
        verify(postRepository).save(captor.capture());
        var postCaptor = captor.getValue();
        assertNotNull(postCaptor);
        assertEquals(postRequest.authorId(), postCaptor.getAuthor().getId());
        assertEquals(postRequest.title(), postCaptor.getTitle());
        assertEquals(postRequest.body(), postCaptor.getBody());
        assertEquals(savedPost.getId(), result.id());
        assertEquals(savedPost.getDate(), result.date());
        assertEquals(savedPost.getTitle(), result.title());
        assertEquals(savedPost.getBody(), result.body());
        assertEquals(savedPost.getAuthor().getId(), result.author().id());
    }

    @Test
    @DisplayName("Should throw exception if user noot found in create post")
    void shouldThrowExceptionIfUserNotFound() {
        //Arrange
        when(userRepository.findById(savedUser.getId())).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.create(postRequest));
    }

    @Test
    @DisplayName("Should find post by id successfully")
    void shouldFindPostById() {
        //Arrange
        when(postRepository.findById(savedPost.getId())).thenReturn(Optional.of(savedPost));
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
    @DisplayName("Should throw exception when post not found")
    void shouldThrowExceptionWhenPostNotFound() {
        //Arrange
        when(postRepository.findById(savedPost.getId())).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.findById(savedPost.getId()));
    }

    @Test
    @DisplayName("Should find post by user id")
    void shouldFindPostByUserId() {
        //Arrange
        Post savedPost01 = new Post(UUID.randomUUID().toString(), Instant.now(), "Partiu viagem", "Vou viajar para São Paulo. Abraços!", savedUser);
        Post savedPost02 = new Post(UUID.randomUUID().toString(), Instant.now(), "Bom dia", "Acordei feliz hoje!", savedUser);
        List<Post> postResponseList = List.of(savedPost, savedPost01, savedPost02);
        when(postRepository.findByAuthorId(savedUser.getId())).thenReturn(postResponseList);
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
        Comment savedComment = new Comment(UUID.randomUUID().toString(), "Muito bom, adorei o post!", savedUser, savedPost);
        Comment savedComment01 = new Comment(UUID.randomUUID().toString(), "Boa viagem mano!", savedUser, savedPost);
        Comment savedComment02 = new Comment(UUID.randomUUID().toString(), "Aproveite!", savedUser, savedPost);
        List<Comment> commentList = List.of(savedComment, savedComment01, savedComment02);
        when(postRepository.findById(savedPost.getId())).thenReturn(Optional.of(savedPost));
        when(commentRepository.findByPostId(savedPost.getId())).thenReturn(commentList);
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
        assertEquals(commentsFound.get().getAuthor().getName(), returnComments.get().author().name());
        assertEquals(commentsFound.get().getPost().getId(), returnComments.get().postId());
    }

    @Test
    @DisplayName("Should throw exception if post with comments not found")
    void shouldThrowExceptionIfPostWithCommentsNotFound() {
        //Arrange
        when(postRepository.findById(savedPost.getId())).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.listAllComments(savedPost.getId()));
    }

    @Test
    @DisplayName("Should update post")
    void shouldUpdatePost() {
        //Arrange
        when(postRepository.findById(savedPost.getId())).thenReturn(Optional.of(savedPost));
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
        //Act
        var result = postService.update(savedPost.getId(), postRequest);
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
        assertEquals(postRequest.title(), postCaptor.getTitle());
        assertEquals(postRequest.body(), postCaptor.getBody());
        assertEquals(postRequest.authorId(), postCaptor.getAuthor().getId());
    }

    @Test
    @DisplayName("Should throw exception when update post")
    void shouldThrowExceptionWhenUpdatePost() {
        //Arrange
        when(postRepository.findById(savedPost.getId())).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.update(savedPost.getId(), postRequest));
    }

    @Test
    @DisplayName("Should delete post successfully")
    void shouldDeletePost() {
        //Arrange
        when(postRepository.findById(savedPost.getId())).thenReturn(Optional.of(savedPost));
        doNothing().when(postRepository).delete(savedPost);
        //Act
        postService.delete(savedPost.getId());
        //Assert
        verify(postRepository).findById(savedPost.getId());
        verify(postRepository).delete(savedPost);
    }

    @Test
    @DisplayName("Should throw exception when delete post")
    void shouldThrowExceptionWhenDeletePost() {
        //Arrange
        when(postRepository.findById(savedPost.getId())).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.delete(savedPost.getId()));
    }
}