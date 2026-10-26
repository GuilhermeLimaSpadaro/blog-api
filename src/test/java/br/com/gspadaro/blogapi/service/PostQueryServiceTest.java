package br.com.gspadaro.blogapi.service;

import br.com.gspadaro.blogapi.dto.comment.CommentResponseDTO;
import br.com.gspadaro.blogapi.dto.post.PostResponseDTO;
import br.com.gspadaro.blogapi.dto.user.UserDetailsDTO;
import br.com.gspadaro.blogapi.dto.user.UserResponseDTO;
import br.com.gspadaro.blogapi.exception.ResourceNotFoundException;
import br.com.gspadaro.blogapi.mapper.custom.PostMapper;
import br.com.gspadaro.blogapi.mapper.custom.UserMapper;
import br.com.gspadaro.blogapi.model.Post;
import br.com.gspadaro.blogapi.model.User;
import br.com.gspadaro.blogapi.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostQueryServiceTest {
    @Mock
    private PostService postService;
    @Mock
    private CommentService commentService;
    @InjectMocks
    private PostQueryService postQueryService;
    @Spy
    private PostMapper postMapper = Mappers.getMapper(PostMapper.class);
    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private PostResponseDTO savedPost;
    private UserResponseDTO userDetails;

    @BeforeEach
    void setUp() {
        userDetails = new UserResponseDTO(UUID.randomUUID().toString(), "Guilherme", "guilhermespadaro@gmail.com", "11955555555");
        savedPost = new PostResponseDTO(UUID.randomUUID().toString(), Instant.now(), "Bom dia!", "Como o dia esta lindo hoje!", userMapper.toDetailsDTO(userDetails));
    }

    @Test
    @DisplayName("Should find post with comments")
    void shouldListPostWithComment() {
        //Arrange
        CommentResponseDTO savedComment = new CommentResponseDTO(UUID.randomUUID().toString(), "Muito bom, adorei o post!", Instant.now(), userDetails.id(), savedPost.id());
        CommentResponseDTO savedComment01 = new CommentResponseDTO(UUID.randomUUID().toString(), "Boa viagem mano!", Instant.now(), userDetails.id(), savedPost.id());
        CommentResponseDTO savedComment02 = new CommentResponseDTO(UUID.randomUUID().toString(), "Aproveite!", Instant.now(), userDetails.id(), savedPost.id());
        List<CommentResponseDTO> commentList = List.of(savedComment, savedComment01, savedComment02);
        when(postService.findById(savedPost.id())).thenReturn(savedPost);
        when(commentService.findByPostId(savedPost.id())).thenReturn(commentList);
        //Act
        var result = postQueryService.findWithComments(savedPost.id());
        //Assert
        verify(postService).findById(savedPost.id());
        verify(commentService).findByPostId(savedPost.id());
        assertNotNull(result);
        assertEquals(savedPost.id(), result.post().id());
        assertEquals(savedPost.date(), result.post().date());
        assertEquals(savedPost.title(), result.post().title());
        assertEquals(savedPost.body(), result.post().body());
        assertEquals(savedPost.user().id(), result.post().user().id());
        var commentsFound = commentList.stream().filter(comment -> savedPost.id().equals(comment.postId())).findFirst();
        var returnComments = result.comments().stream().filter(commentResponseDTO -> savedPost.id().equals(commentResponseDTO.postId())).findFirst();
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
        when(postService.findById(savedPost.id())).thenThrow(new ResourceNotFoundException("Post not found"));
        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postQueryService.findWithComments(savedPost.id()));
    }
}
