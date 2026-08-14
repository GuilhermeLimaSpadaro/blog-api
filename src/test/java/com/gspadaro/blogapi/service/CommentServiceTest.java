package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.Comment;
import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.CommentRequestDTO;
import com.gspadaro.blogapi.dto.CommentResponseDTO;
import com.gspadaro.blogapi.dto.PostRequestDTO;
import com.gspadaro.blogapi.repository.CommentRepository;
import com.gspadaro.blogapi.repository.PostRepository;
import com.gspadaro.blogapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
class CommentServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentService commentService;
    @Captor
    private ArgumentCaptor<Comment> captor;

    private User savedUser;
    private Post savedPost;
    private Comment savedComment;
    private CommentRequestDTO commentRequest;
    @BeforeEach
    void setUp() {
        savedUser = new User(UUID.randomUUID().toString(), "Guilherme", "guilhermespadaro@gmail.com", "11955447766", "13ABC234");
        savedPost = new Post(UUID.randomUUID().toString(), Instant.now(), "Bom dia!", "Como o dia está lindo hoje!", savedUser);
        savedComment = new Comment(UUID.randomUUID().toString(), "Muito bom, adorei o post!", savedUser, savedPost);
        commentRequest = new CommentRequestDTO("Muito bom, adorei o post!", savedUser.getId(), savedPost.getId());
    }

    @Test
    void create() {
        //Arrange
        doReturn(Optional.of(savedUser)).when(userRepository).findById(commentRequest.authorId());
        doReturn(Optional.of(savedPost)).when(postRepository).findById(commentRequest.postId());
        doReturn(savedComment).when(commentRepository).save(any(Comment.class));
        //Act
        var result = commentService.create(commentRequest);
        //Assert
        verify(userRepository).findById(savedUser.getId());
        verify(postRepository).findById(savedPost.getId());
        verify(commentRepository).save(captor.capture());
        var capturedComment = captor.getValue();
        assertNotNull(result);
        assertEquals(savedComment.getId(), result.id());
        assertEquals(savedComment.getText(), result.text());
        assertEquals(savedComment.getDate(), result.date());
        assertEquals(savedComment.getAuthor().getId(), result.author().id());
        assertEquals(savedComment.getPost().getId(), result.postId());
        assertEquals(commentRequest.text(), capturedComment.getText());
        assertEquals(commentRequest.authorId(), capturedComment.getAuthor().getId());
        assertEquals(commentRequest.postId(), capturedComment.getPost().getId());
    }

    @Test
    void update() {
        //Arrange

        //Act

        //Assert
    }

    @Test
    void delete() {
        //Arrange

        //Act

        //Assert
    }

    @Test
    void findAll() {
        //Arrange

        //Act

        //Assert
    }
}