package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.UserRequestDTO;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private User saved;
    private UserRequestDTO input;

    @BeforeEach
    void setUp() {
        saved = new User(UUID.randomUUID().toString(), "Guilherme", "guilhermespadaro@gmail.com", "11955447766", "13ABC234");
        input = new UserRequestDTO("Guilherme", "guilhermespadaro@gmail.com", "1177886655", "22@@ABC66");
    }

    @Test
    @DisplayName("Should create a user successfully.")
    void shouldCreateUser() {
        //Arrange
        doReturn(saved).when(userRepository).save(userCaptor.capture());
        //Act
        var result = userService.create(input);
        //Assert
        var userCaptured = userCaptor.getValue();
        verify(userRepository).save(userCaptured);
        assertNotNull(result);
        assertEquals(saved.getId(), result.id());
        assertEquals(saved.getName(), result.name());
        assertEquals(input.name(), userCaptured.getName());
        assertEquals(input.email(), userCaptured.getEmail());
        assertEquals(input.phone(), userCaptured.getPhone());
    }

    @Test
    @DisplayName("User deletion must have been successful.")
    void shouldDeleteUser() {
        //Arrange
        doReturn(Optional.of(saved)).when(userRepository).findById(saved.getId());
        doNothing().when(userRepository).delete(userCaptor.capture());
        //Act
        userService.delete(saved.getId());
        //Assert
        verify(userRepository).delete(saved);
    }

    @Test
    @DisplayName("User find by id must have been successful.")
    void shouldFindUserById(){
        //Arrange
        doReturn(Optional.of(saved)).when(userRepository).findById(saved.getId());
        //Act
        var result = userService.findById(saved.getId());
        //Assert
        verify(userRepository).findById(saved.getId());
        assertNotNull(result);
        assertEquals(saved.getId(), result.id());
        assertEquals(saved.getName(), result.name());
        assertEquals(saved.getEmail(), result.email());
        assertEquals(saved.getPhone(), result.phone());
    }
}