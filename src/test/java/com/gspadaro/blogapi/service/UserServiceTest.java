package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.UserRequestDTO;
import com.gspadaro.blogapi.exception.ResourceNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> captor;

    private User savedUser;
    private UserRequestDTO requestUser;

    @BeforeEach
    void setUp() {
        savedUser = new User(UUID.randomUUID().toString(), "Guilherme", "guilhermespadaro@gmail.com", "11955447766", "13ABC234");
        requestUser = new UserRequestDTO("Bob Grey", "bob@gmail.com", "22222", "11111");
    }

    @Test
    @DisplayName("Should create a user successfully.")
    void shouldCreateUser() {
        //Arrange
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        //Act
        var result = userService.create(requestUser);
        //Assert
        verify(userRepository).save(captor.capture());
        var userCaptor = captor.getValue();
        assertNotNull(result);
        assertEquals(savedUser.getId(), result.id());
        assertEquals(savedUser.getName(), result.name());
        assertEquals(requestUser.name(), userCaptor.getName());
        assertEquals(requestUser.email(), userCaptor.getEmail());
        assertEquals(requestUser.phone(), userCaptor.getPhone());
    }

    @Test
    @DisplayName("Should find user by id successfully")
    void shouldFindUserById() {
        //Arrange
        when(userRepository.findById(savedUser.getId())).thenReturn(Optional.of(savedUser));
        //Act
        var result = userService.findById(savedUser.getId());
        //Assert
        verify(userRepository).findById(savedUser.getId());
        assertNotNull(result);
        assertEquals(savedUser.getId(), result.id());
        assertEquals(savedUser.getName(), result.name());
        assertEquals(savedUser.getEmail(), result.email());
        assertEquals(savedUser.getPhone(), result.phone());
    }

    @Test
    @DisplayName("Should Throw Exception When User Not Found")
    void shouldThrowExceptionWhenUserNotFound() {
        //Arrange
        when(userRepository.findById(savedUser.getId())).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(savedUser.getId()));
    }

    @Test
    @DisplayName("Should update user")
    void shouldUpdateUser() {
        //Arrange
        when(userRepository.findById(savedUser.getId())).thenReturn(Optional.of(savedUser));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        //Act
        var result = userService.update(savedUser.getId(), requestUser);
        //Assert
        verify(userRepository).findById(savedUser.getId());
        verify(userRepository).save(captor.capture());
        var userCaptor = captor.getValue();
        assertNotNull(result);
        assertEquals(savedUser.getId(), result.id());
        assertEquals(savedUser.getName(), result.name());
        assertEquals(savedUser.getEmail(), result.email());
        assertEquals(savedUser.getPhone(), result.phone());
        assertEquals(requestUser.name(), userCaptor.getName());
        assertEquals(requestUser.email(), userCaptor.getEmail());
        assertEquals(requestUser.phone(), userCaptor.getPhone());
    }

    @Test
    @DisplayName("Should Throw Exception When Update User")
    void shouldThrowExceptionWhenUpdateUser() {
        //Arrange
        when(userRepository.findById(savedUser.getId())).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.update(savedUser.getId(), requestUser));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUser() {
        //Arrange
        when(userRepository.findById(savedUser.getId())).thenReturn(Optional.of(savedUser));
        doNothing().when(userRepository).delete(savedUser);
        //Act
        userService.delete(savedUser.getId());
        //Assert
        verify(userRepository).findById(savedUser.getId());
        verify(userRepository).delete(savedUser);
    }

    @Test
    @DisplayName("Should Throw Exception When Delete User")
    void shouldThrowExceptionWhenDeleteUser() {
        //Arrange
        when(userRepository.findById(savedUser.getId())).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.delete(savedUser.getId()));
    }
}