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
    private ArgumentCaptor<User> userArgumentCaptor;

    private User saved;
    private UserRequestDTO userInput;

    @BeforeEach
    void setUp() {
        saved = new User(UUID.randomUUID().toString(), "Guilherme", "guilhermespadaro@gmail.com", "11955447766", "13ABC234");
        userInput = new UserRequestDTO("Guilherme", "guilhermespadaro@gmail.com", "1177886655", "22@@ABC66");
    }

    @Test
    @DisplayName("Should create a user successfully.")
    void shouldCreateAUserSuccessfully() {
        //Arrange
        doReturn(saved).when(userRepository).save(userArgumentCaptor.capture());
        //Act
        var output = userService.create(userInput);
        //Assert
        verify(userRepository).save(saved);
        assertNotNull(output);
        assertEquals(saved.getId(), output.id());
        assertEquals(saved.getName(), output.name());
        var userCaptured = userArgumentCaptor.getValue();
        assertEquals(userInput.name(), userCaptured.getName());
        assertEquals(userInput.email(), userCaptured.getEmail());
        assertEquals(userInput.phone(), userCaptured.getPhone());
    }

    @Test
    @DisplayName("User deletion must have been successful")
    void userDeletionMustHaveBeenSuccessful() {
        //Arrange
        doReturn(Optional.of(saved)).when(userRepository).findById(saved.getId());
        doNothing().when(userRepository).delete(userArgumentCaptor.capture());
        //Act
        userService.delete(saved.getId());
        //Assert
        verify(userRepository).delete(saved);
    }

    

}