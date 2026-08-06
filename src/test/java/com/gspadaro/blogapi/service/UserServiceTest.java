package com.gspadaro.blogapi.service;

import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.UserRequestDTO;
import com.gspadaro.blogapi.repository.PostRepository;
import com.gspadaro.blogapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository uRepository;

    @Mock
    private PostRepository pRepository;

    @InjectMocks
    private UserService uService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    void setUp() {

    }

    @Nested
    class create {

        @Test
        @DisplayName("Should create a user successfully.")
        void shouldCreateAUserSuccessfully() {
            //Arrange
            var saved = new User(UUID.randomUUID().toString()
                    , "Guilherme"
                    , "guilhermespadaro@gmail.com"
                    , "11955447766"
                    , "13ABC234");
            doReturn(saved).when(uRepository).save(userArgumentCaptor.capture());
            var input = new UserRequestDTO("Guilherme",
                    "guilhermespadaro@gmail.com"
                    , "1177886655"
                    , "22@@ABC66");
            //Act
            var output = uService.create(input);
            //Assert
            assertNotNull(output);
            assertEquals(saved.getId(), output.id());
            assertEquals(saved.getName(), output.name());

            var userCaptured = userArgumentCaptor.getValue();
            assertEquals(input.name(), userCaptured.getName());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.phone(), userCaptured.getPhone());
        }
    }


}