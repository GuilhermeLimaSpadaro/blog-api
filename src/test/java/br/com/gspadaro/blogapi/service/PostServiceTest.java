package br.com.gspadaro.blogapi.service;

import br.com.gspadaro.blogapi.dto.comment.CommentResponseDTO;
import br.com.gspadaro.blogapi.dto.post.PostRequestDTO;
import br.com.gspadaro.blogapi.dto.post.PostResponseDTO;
import br.com.gspadaro.blogapi.dto.user.UserResponseDTO;
import br.com.gspadaro.blogapi.exception.ResourceNotFoundException;
import br.com.gspadaro.blogapi.mapper.custom.PostMapper;
import br.com.gspadaro.blogapi.mapper.custom.UserMapper;
import br.com.gspadaro.blogapi.model.Post;
import br.com.gspadaro.blogapi.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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
    @InjectMocks
    private PostService postService;
    @Captor
    private ArgumentCaptor<Post> captor;
    @Spy
    private PostMapper postMapper = Mappers.getMapper(PostMapper.class);
    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

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
        when(userService.findById(postRequest.userId())).thenReturn(userDetails);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
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
        when(userService.findById(postRequest.userId())).thenThrow(new ResourceNotFoundException("User not found"));
        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.create(postRequest));
        verify(postRepository, never()).save(any(Post.class));
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