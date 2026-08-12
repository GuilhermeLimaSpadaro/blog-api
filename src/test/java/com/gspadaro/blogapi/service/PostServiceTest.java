package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.PostRequestDTO;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private PostService postService;
    @Captor
    private ArgumentCaptor<Post> captor;

    private User user;
    private Post saved;
    private PostRequestDTO input;

    @BeforeEach
    void setUp() {
        user = new User(UUID.randomUUID().toString(), "Guilherme", "guilhermespadaro@gmail.com", "11955447766", "13ABC234");
        saved = new Post(UUID.randomUUID().toString(), Instant.now(), "Bom dia!", "Como o dia está lindo hoje!", user);
        input = new PostRequestDTO("Bom dia!", "Como o dia está lindo hoje!", user.getId());
    }

    @Test
    @DisplayName("Should create a post successfully.")
    void shouldCreatePost() {
        //Arrange
        doReturn(Optional.of(user)).when(userRepository).findById(input.authorId());
        doReturn(saved).when(postRepository).save(any(Post.class));
        //Act
        var result = postService.create(input);
        //Assert
        verify(postRepository).save(captor.capture());
        var postCapture = captor.getValue();
        assertNotNull(result);
        assertEquals(input.authorId(), postCapture.getAuthor().getId());
        assertEquals(input.title(), postCapture.getTitle());
        assertEquals(input.body(), postCapture.getBody());
        assertEquals(saved.getId(), result.id());
        assertEquals(saved.getDate(), result.date());
        assertEquals(saved.getTitle(), result.title());
        assertEquals(saved.getBody(), result.body());
        assertEquals(saved.getAuthor().getId(), result.author().id());
    }

    @Test
    @DisplayName("Should delete post successfully")
    void shouldDeletePost() {
        //Arrange

        //Act

        //Assert
    }

    @Test
    @DisplayName("Should update post")
    void shouldUpdatePost() {
        //Arrange

        //Act

        //Assert
    }

    @Test
    @DisplayName("Should find post by id successfully")
    void shouldFindPostById() {
        //Arrange

        //Act

        //Assert

    }
}