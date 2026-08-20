package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.dto.CommentResponseDTO;
import com.gspadaro.blogapi.dto.PostRequestDTO;
import com.gspadaro.blogapi.dto.PostResponseDTO;
import com.gspadaro.blogapi.dto.UserDetailsDTO;
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

    private UserDetailsDTO userDetails;
    private Post savedPost;
    private PostRequestDTO postRequest;

    @BeforeEach
    void setUp() {
        userDetails = new UserDetailsDTO(UUID.randomUUID().toString(), "Guilherme");
        savedPost = new Post(UUID.randomUUID().toString(), Instant.now(), "Bom dia!", "Como o dia esta lindo hoje!", userDetails.id());
        postRequest = new PostRequestDTO("Bom tarde!", "Vamos tomar um cafe?!", userDetails.id());
    }

    @Test
    @DisplayName("Should create a post successfully.")
    void shouldCreatePost() {
        //Arrange
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
        when(userService.findDetailsById(postRequest.authorId())).thenReturn(userDetails);
        //Act
        var result = postService.create(postRequest);
        //Assert
        verify(userService).findDetailsById(postRequest.authorId());
        verify(postRepository).save(captor.capture());
        var postCaptor = captor.getValue();
        assertNotNull(postCaptor);
        assertEquals(postRequest.authorId(), postCaptor.getAuthorId());
        assertEquals(postRequest.title(), postCaptor.getTitle());
        assertEquals(postRequest.body(), postCaptor.getBody());
        assertEquals(savedPost.getId(), result.id());
        assertEquals(savedPost.getDate(), result.date());
        assertEquals(savedPost.getTitle(), result.title());
        assertEquals(savedPost.getBody(), result.body());
        assertEquals(savedPost.getAuthorId(), result.author().id());
    }

    @Test
    @DisplayName("Should throw exception if user not found in create post")
    void shouldThrowExceptionIfUserNotFound() {
        //Arrange
        when(userService.findDetailsById(userDetails.id())).thenReturn(userDetails);
        //Act & Assert
        assertThrows(NullPointerException.class, () -> postService.create(postRequest));
    }

    @Test
    @DisplayName("Should find post by id successfully")
    void shouldFindPostById() {
        //Arrange
        when(postRepository.findById(savedPost.getId())).thenReturn(Optional.of(savedPost));
        when(userService.findDetailsById(savedPost.getAuthorId())).thenReturn(userDetails);
        //Act
        var result = postService.findById(savedPost.getId());
        //Assert
        verify(userService).findDetailsById(savedPost.getAuthorId());
        verify(postRepository).findById(savedPost.getId());
        assertEquals(savedPost.getId(), result.id());
        assertEquals(savedPost.getDate(), result.date());
        assertEquals(savedPost.getTitle(), result.title());
        assertEquals(savedPost.getBody(), result.body());
        assertEquals(savedPost.getAuthorId(), result.author().id());
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
        when(postRepository.findByAuthorId(userDetails.id())).thenReturn(postResponseList);
        when(userService.findDetailsById(userDetails.id())).thenReturn(userDetails);
        //Act
        var result = postService.findByAuthorId(userDetails.id());
        //Assert
        verify(postRepository).findByAuthorId(userDetails.id());
        verify(userService).findDetailsById(userDetails.id());
        assertNotNull(result);
        Optional<Post> postFound = postResponseList.stream().filter(post -> userDetails.id().equals(post.getAuthorId())).findFirst();
        Optional<PostResponseDTO> returnPost = result.stream().filter(post -> userDetails.id().equals(post.author().id())).findFirst();
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
        when(userService.findDetailsById(savedPost.getAuthorId())).thenReturn(userDetails);
        when(commentService.findAllCommentsByPostId(savedPost.getId())).thenReturn(commentList);
        //Act
        var result = postService.listAllComments(savedPost.getId());
        //Assert
        verify(postRepository).findById(savedPost.getId());
        verify(userService).findDetailsById(savedPost.getAuthorId());
        verify(commentService).findAllCommentsByPostId(savedPost.getId());
        assertNotNull(result);
        assertEquals(savedPost.getId(), result.post().id());
        assertEquals(savedPost.getDate(), result.post().date());
        assertEquals(savedPost.getTitle(), result.post().title());
        assertEquals(savedPost.getBody(), result.post().body());
        assertEquals(savedPost.getAuthorId(), result.post().author().id());
        assertEquals(userDetails.id(), result.post().author().id());
        assertEquals(userDetails.name(), result.post().author().name());
        var commentsFound = commentList.stream().filter(comment -> savedPost.getId().equals(comment.postId())).findFirst();
        var returnComments = result.comment().stream().filter(commentResponseDTO -> savedPost.getId().equals(commentResponseDTO.postId())).findFirst();
        assertTrue(commentsFound.isPresent());
        assertTrue(returnComments.isPresent());
        assertEquals(commentsFound.get().id(), returnComments.get().id());
        assertEquals(commentsFound.get().text(), returnComments.get().text());
        assertEquals(commentsFound.get().date(), returnComments.get().date());
        assertEquals(commentsFound.get().authorId(), returnComments.get().authorId());
        assertEquals(commentsFound.get().postId(), returnComments.get().postId());
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
        when(userService.findDetailsById(savedPost.getAuthorId())).thenReturn(userDetails);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
        //Act
        var result = postService.update(savedPost.getId(), postRequest);
        //Assert
        verify(postRepository).findById(savedPost.getId());
        verify(userService).findDetailsById(savedPost.getAuthorId());
        verify(postRepository).save(captor.capture());
        var postCaptor = captor.getValue();
        assertNotNull(postCaptor);
        assertEquals(savedPost.getId(), result.id());
        assertEquals(savedPost.getDate(), result.date());
        assertEquals(savedPost.getTitle(), result.title());
        assertEquals(savedPost.getBody(), result.body());
        assertEquals(savedPost.getAuthorId(), result.author().id());
        assertEquals(postRequest.title(), postCaptor.getTitle());
        assertEquals(postRequest.body(), postCaptor.getBody());
        assertEquals(postRequest.authorId(), postCaptor.getAuthorId());
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