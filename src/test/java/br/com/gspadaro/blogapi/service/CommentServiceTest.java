package br.com.gspadaro.blogapi.service;

import br.com.gspadaro.blogapi.dto.comment.CommentRequestDTO;
import br.com.gspadaro.blogapi.dto.post.PostResponseDTO;
import br.com.gspadaro.blogapi.dto.user.UserResponseDTO;
import br.com.gspadaro.blogapi.exception.ResourceNotFoundException;
import br.com.gspadaro.blogapi.mapper.custom.CommentMapper;
import br.com.gspadaro.blogapi.mapper.custom.PostMapper;
import br.com.gspadaro.blogapi.mapper.custom.UserMapper;
import br.com.gspadaro.blogapi.model.Comment;
import br.com.gspadaro.blogapi.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
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
    @Mock
    private UserService userService;
    @Mock
    private PostService postService;
    @InjectMocks
    private CommentService commentService;
    @Captor
    private ArgumentCaptor<Comment> captor;
    @Spy
    private PostMapper postMapper = Mappers.getMapper(PostMapper.class);
    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    @Spy
    private CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    private UserResponseDTO savedUser;
    private PostResponseDTO savedPost;
    private Comment savedComment;
    private CommentRequestDTO commentRequest;

    @BeforeEach
    void setUp() {
        savedUser = new UserResponseDTO(UUID.randomUUID().toString(), "Guilherme", "guilhermespadaro@gmail.com", "11955447766");
        savedPost = new PostResponseDTO(UUID.randomUUID().toString(), Instant.now(), "Bom dia!", "Como o dia está lindo hoje!", userMapper.toDetailsDTO(savedUser));
        savedComment = new Comment(UUID.randomUUID().toString(), "Andar de skate é demais!", Instant.now(), savedUser.id(), savedPost.id());
        commentRequest = new CommentRequestDTO("Que cachorro lindo!", savedUser.id(), savedPost.id());
    }

    @Test
    @DisplayName("Should create a comment successfully.")
    void shouldCreateComment() {
        //Arrange
        when(userService.findById(commentRequest.userId())).thenReturn(savedUser);
        when(postService.findById(commentRequest.postId())).thenReturn(savedPost);
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
        assertEquals(savedComment.getUserId(), result.userId());
        assertEquals(savedComment.getPostId(), result.postId());
        assertEquals(commentRequest.text(), commentCaptor.getText());
        assertEquals(commentRequest.userId(), commentCaptor.getUserId());
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
        assertEquals(savedComment.getUserId(), result.userId());
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
        assertEquals(savedComment.getUserId(), result.userId());
        assertEquals(savedComment.getPostId(), result.postId());
        assertEquals(commentRequest.text(), commentCaptor.getText());
        assertEquals(commentRequest.userId(), commentCaptor.getUserId());
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