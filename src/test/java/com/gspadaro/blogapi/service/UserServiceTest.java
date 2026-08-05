package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.UserRequestDTO;
import com.gspadaro.blogapi.repository.PostRepository;
import com.gspadaro.blogapi.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository uRepository;

    @Mock
    private PostRepository pRepository;

    @InjectMocks
    private UserService uService;

    @Test
    @DisplayName("Deve criar um usuario")
    void create() {

        //Arrange
        var saved = new User("1", "Guilherme", "guilhermespadaro@gmail.com");
        doReturn(saved).when(uRepository).save(any());
        var input = new UserRequestDTO("Guilherme", "guilhermespadaro@gmail.com");
        //Act
        var result = uService.create(input);
        //Assert
        assertEquals("1", result.id());
        assertEquals("Guilherme", result.name());
        assertEquals("guilhermespadaro@gmail.com", result.email());
    }
}