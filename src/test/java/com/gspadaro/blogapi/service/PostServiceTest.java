package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.dto.user.UserResponseDTO;
import com.gspadaro.blogapi.model.Post;
import com.gspadaro.blogapi.dto.comment.CommentResponseDTO;
import com.gspadaro.blogapi.dto.post.PostRequestDTO;
import com.gspadaro.blogapi.dto.post.PostResponseDTO;
import com.gspadaro.blogapi.dto.user.UserDetailsDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.repository.PostRepository;
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
    private UserService userService;
    @Mock
    private CommentService commentService;
    @InjectMocks
    private PostService postService;
    @Captor
    private ArgumentCaptor<Post> captor;

    private UserResponseDTO userDetails;
    private Post savedPost;
    private PostRequestDTO postRequest;

    @BeforeEach
    void setUp() {
        userDetails = new UserResponseDTO(UUID.randomUUID().toString(), "Guilherme", "guilhermespadaro@gmail.com", "11955555555");
        savedPost = new Post(UUID.randomUUID().toString(), Instant.now(), "Bom dia!", "Como o dia esta lindo hoje!", userDetails.id());
        postRequest = new PostRequestDTO("Bom tarde!", "Vamos tomar um cafe?!", userDetails.id());
    }

    @Test
    @DisplayName("Should create a post successfully.")
    void shouldCreatePost() {
        //Arrange
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
        when(userService.findById(postRequest.userId())).thenReturn(userDetails);
        //Act
        var result = postService.create(postRequest);
        //Assert
        verify(postRepository).save(captor.capture());
        verify(userService).findById(postRequest.userId());
        var postCaptor = captor.getValue();
        assertNotNull(postCaptor);
        assertEquals(postRequest.userId(), postCaptor.getUserId());
        assertEquals(postRequest.title(), postCaptor.getTitle());
        assertEquals(postRequest.body(), postCaptor.getBody());
        assertEquals(savedPost.getId(), result.id());
        assertEquals(savedPost.getDate(), result.date());
        assertEquals(savedPost.getTitle(), result.title());
        assertEquals(savedPost.getBody(), result.body());
        assertEquals(savedPost.getUserId(), result.user().id());
    }

    @Test
    @DisplayName("Should throw exception if user not found in create post")
    void shouldThrowExceptionIfUserNotFound() {
        //Arrange
        when(userService.findById(userDetails.id())).thenReturn(userDetails);
        //Act & Assert
        assertThrows(NullPointerException.class, () -> postService.create(postRequest));
    }

    @Test
    @DisplayName("Should find post by id successfully")
    void shouldFindPostById() {
        //Arrange
        when(postRepository.findById(savedPost.getId())).thenReturn(Optional.of(savedPost));
        when(userService.findById(savedPost.getUserId())).thenReturn(userDetails);
        //Act
        var result = postService.findById(savedPost.getId());
        //Assert
        verify(userService).findById(savedPost.getUserId());
        verify(postRepository).findById(savedPost.getId());
        assertEquals(savedPost.getId(), result.id());
        assertEquals(savedPost.getDate(), result.date());
        assertEquals(savedPost.getTitle(), result.title());
        assertEquals(savedPost.getBody(), result.body());
        assertEquals(savedPost.getUserId(), result.user().id());
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
        Post savedPost01 = new Post(UUID.randomUUID().toString(), Instant.now(), "Partiu viagem", "Vou viajar para São Paulo. Abraços!", userDetails.id());
        Post savedPost02 = new Post(UUID.randomUUID().toString(), Instant.now(), "Bom dia", "Acordei feliz hoje!", userDetails.id());
        List<Post> postResponseList = List.of(savedPost, savedPost01, savedPost02);
        when(postRepository.findByUserId(userDetails.id())).thenReturn(postResponseList);
        when(userService.findById(userDetails.id())).thenReturn(userDetails);
        //Act
        var result = postService.findByUserId(userDetails.id());
        //Assert
        verify(postRepository).findByUserId(userDetails.id());
        verify(userService).findById(userDetails.id());
        assertNotNull(result);
        Optional<Post> postFound = postResponseList.stream().filter(post -> userDetails.id().equals(post.getUserId())).findFirst();
        Optional<PostResponseDTO> returnPost = result.stream().filter(post -> userDetails.id().equals(post.user().id())).findFirst();
        assertTrue(postFound.isPresent());
        assertTrue(returnPost.isPresent());
        assertEquals(postFound.get().getId(), returnPost.get().id());
    }

    @Test
    @DisplayName("Should find post with comments")
    void shouldListPostWithComment() {
        //Arrange
        CommentResponseDTO savedComment = new CommentResponseDTO(UUID.randomUUID().toString(), "Muito bom, adorei o post!", Instant.now(), userDetails.id(), savedPost.getId());
        CommentResponseDTO savedComment01 = new CommentResponseDTO(UUID.randomUUID().toString(), "Boa viagem mano!", Instant.now(), userDetails.id(), savedPost.getId());
        CommentResponseDTO savedComment02 = new CommentResponseDTO(UUID.randomUUID().toString(), "Aproveite!", Instant.now(), userDetails.id(), savedPost.getId());
        List<CommentResponseDTO> commentList = List.of(savedComment, savedComment01, savedComment02);
        when(postRepository.findById(savedPost.getId())).thenReturn(Optional.of(savedPost));
        when(userService.findById(savedPost.getUserId())).thenReturn(userDetails);
        when(commentService.findByPostId(savedPost.getId())).thenReturn(commentList);
        //Act
        var result = postService.findWithComments(savedPost.getId());
        //Assert
        verify(postRepository).findById(savedPost.getId());
        verify(userService).findById(savedPost.getUserId());
        verify(commentService).findByPostId(savedPost.getId());
        assertNotNull(result);
        assertEquals(savedPost.getId(), result.post().id());
        assertEquals(savedPost.getDate(), result.post().date());
        assertEquals(savedPost.getTitle(), result.post().title());
        assertEquals(savedPost.getBody(), result.post().body());
        assertEquals(savedPost.getUserId(), result.post().user().id());
        assertEquals(userDetails.id(), result.post().user().id());
        assertEquals(userDetails.name(), result.post().user().name());
        var commentsFound = commentList.stream().filter(comment -> savedPost.getId().equals(comment.postId())).findFirst();
        var returnComments = result.comments().stream().filter(commentResponseDTO -> savedPost.getId().equals(commentResponseDTO.postId())).findFirst();
        assertTrue(commentsFound.isPresent());
        assertTrue(returnComments.isPresent());
        assertEquals(commentsFound.get().id(), returnComments.get().id());
        assertEquals(commentsFound.get().text(), returnComments.get().text());
        assertEquals(commentsFound.get().date(), returnComments.get().date());
        assertEquals(commentsFound.get().userId(), returnComments.get().userId());
        assertEquals(commentsFound.get().postId(), returnComments.get().postId());
    }

    @Test
    @DisplayName("Should throw exception if post with comments not found")
    void shouldThrowExceptionIfPostWithCommentsNotFound() {
        //Arrange
        when(postRepository.findById(savedPost.getId())).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.findWithComments(savedPost.getId()));
    }

    @Test
    @DisplayName("Should update post")
    void shouldUpdatePost() {
        //Arrange
        when(postRepository.findById(savedPost.getId())).thenReturn(Optional.of(savedPost));
        when(userService.findById(savedPost.getUserId())).thenReturn(userDetails);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
        //Act
        var result = postService.update(savedPost.getId(), postRequest);
        //Assert
        verify(postRepository).findById(savedPost.getId());
        verify(userService).findById(savedPost.getUserId());
        verify(postRepository).save(captor.capture());
        var postCaptor = captor.getValue();
        assertNotNull(postCaptor);
        assertEquals(savedPost.getId(), result.id());
        assertEquals(savedPost.getDate(), result.date());
        assertEquals(savedPost.getTitle(), result.title());
        assertEquals(savedPost.getBody(), result.body());
        assertEquals(savedPost.getUserId(), result.user().id());
        assertEquals(postRequest.title(), postCaptor.getTitle());
        assertEquals(postRequest.body(), postCaptor.getBody());
        assertEquals(postRequest.userId(), postCaptor.getUserId());
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