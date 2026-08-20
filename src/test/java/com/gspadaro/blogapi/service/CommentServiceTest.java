package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.Comment;
import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.CommentRequestDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
import com.gspadaro.blogapi.repository.CommentRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
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
        savedPost = new Post(UUID.randomUUID().toString(), Instant.now(), "Bom dia!", "Como o dia está lindo hoje!", savedUser.getId());
        savedComment = new Comment(UUID.randomUUID().toString(), "Andar de skate é demais!", savedUser.getId(), savedPost.getId());
        commentRequest = new CommentRequestDTO("Que cachorro lindo!", savedUser.getId(), savedPost.getId());
    }

    @Test
    @DisplayName("Should create a comment successfully.")
    void shouldCreateComment() {
        //Arrange
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);
        //Act
        var result = commentService.create(commentRequest);
        //Assert
        verify(commentRepository).save(captor.capture());
        var commentCaptor = captor.getValue();
        assertNotNull(result);
        assertEquals(savedComment.getId(), result.id());
        assertEquals(savedComment.getText(), result.text());
        assertEquals(savedComment.getDate(), result.date());
        assertEquals(savedComment.getAuthorId(), result.authorId());
        assertEquals(savedComment.getPostId(), result.postId());
        assertEquals(commentRequest.text(), commentCaptor.getText());
        assertEquals(commentRequest.authorId(), commentCaptor.getAuthorId());
        assertEquals(commentRequest.postId(), commentCaptor.getPostId());
    }

    @Test
    @DisplayName("Should find comment by id")
    void shouldFindCommentById() {
        //Arrange
        when(commentRepository.findById(savedComment.getId())).thenReturn(Optional.of(savedComment));
        //Act
        var result = commentService.findById(savedComment.getId());
        //Assert
        verify(commentRepository).findById(savedComment.getId());
        assertNotNull(result);
        assertEquals(savedComment.getId(), result.id());
        assertEquals(savedComment.getText(), result.text());
        assertEquals(savedComment.getDate(), result.date());
        assertEquals(savedComment.getAuthorId(), result.authorId());
        assertEquals(savedComment.getPostId(), result.postId());
    }

    @Test
    @DisplayName("Should throw exception when comment not found")
    void shouldThrowExceptionWhenCommentNotFound() {
        //Arrange
        when(commentRepository.findById(savedComment.getId())).thenReturn(Optional.empty());
        //Act & //Assert
        assertThrows(ResourceNotFoundException.class, () -> commentService.findById(savedComment.getId()));
    }

    @Test
    @DisplayName("Should update comment")
    void shouldUpdateComment() {
        //Arrange
        when(commentRepository.findById(savedComment.getId())).thenReturn(Optional.of(savedComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);
        //Act
        var result = commentService.update(savedComment.getId(), commentRequest);
        //Assert
        verify(commentRepository).findById(savedComment.getId());
        verify(commentRepository).save(captor.capture());
        assertNotNull(result);
        var commentCaptor = captor.getValue();
        assertEquals(savedComment.getId(), result.id());
        assertEquals(savedComment.getText(), result.text());
        assertEquals(savedComment.getDate(), result.date());
        assertEquals(savedComment.getAuthorId(), result.authorId());
        assertEquals(savedComment.getPostId(), result.postId());
        assertEquals(commentRequest.text(), commentCaptor.getText());
        assertEquals(commentRequest.authorId(), commentCaptor.getAuthorId());
        assertEquals(commentRequest.postId(), commentCaptor.getPostId());
    }

    @Test
    @DisplayName("Should throw exception when update comment")
    void shouldThrowExceptionWhenUpdateComment() {
        //Arrange
        when(commentRepository.findById(savedComment.getId())).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> commentService.update(savedComment.getId(), commentRequest));
    }

    @Test
    @DisplayName("Should delete comment successfully")
    void shouldDeleteComment() {
        //Arrange
        when(commentRepository.findById(savedComment.getId())).thenReturn(Optional.of(savedComment));
        doNothing().when(commentRepository).delete(savedComment);
        //Act
        commentService.delete(savedComment.getId());
        //Assert
        verify(commentRepository).findById(savedComment.getId());
        verify(commentRepository).delete(savedComment);
    }

    @Test
    @DisplayName("Should throw exception when delete comment")
    void shouldThrowExceptionWhenDeleteComment() {
        //Arrange
        when(commentRepository.findById(savedComment.getId())).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> commentService.delete(savedComment.getId()));
    }
}