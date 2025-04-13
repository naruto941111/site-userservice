package com.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.constant.ResponseConstant;
import com.userservice.controller.UserServiceController;
import com.userservice.dto.CommonException;
import com.userservice.dto.request.UserLoginDTO;
import com.userservice.dto.request.UserRegistrationDTO;
import com.userservice.dto.request.UserUpdateDTO;
import com.userservice.dto.response.RestApiResponse;
import com.userservice.model.User;
import com.userservice.repository.UserRepository;
import com.userservice.service.ValidationService;
import com.userservice.service.impl.UserServiceImpl;
import com.userservice.utility.JwtTokenProvider;
import com.userservice.utility.Utility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
//@Sql(scripts = "/script/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserServiceApplicationTests {

    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ValidationService validationService;

    @Mock
    private Logger log;

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private Utility utility;

    private Integer userId = 1;
    private String username = "admin123";
    private String email = "admin@dummy.com";
    private String refId = "ref123";
    private String password = "admin123";
    private String encodedPassword = "$2a$10$QaO0YFRXs8bqivtIaYskEe/Ghoc1HiS7I2M7fd9HBQGM0KjpT3pf2";

    @Test
    void testUserLogin_success() {

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        UserLoginDTO loginDTO = UserLoginDTO.builder().password(password).build();

        User mockUser = User.builder().id(1).username(username).password(encodedPassword).email(email).status(1).build();

        when(userRepository.findByUsernameAndStatus(username, 1))
                .thenReturn(Optional.of(mockUser));

        // Act
        ResponseEntity<RestApiResponse> response = userService.userLogin(username, loginDTO, refId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseConstant.GENERIC_REQUEST_SUCCESS, response.getBody().getResponseCode());
    }

    @Test
    void testUserRegistration_success() {

        when(passwordEncoder.encode(anyString())).thenReturn(password);

        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder().username(username)
                .password(password).confirmPassword(password).emailId(email).build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(null));

        // Act
        ResponseEntity<RestApiResponse> response = (ResponseEntity<RestApiResponse>)
                userService.userRegistration(userRegistrationDTO, refId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseConstant.USER_REGISTERED_SUCCESSFUL, response.getBody().getResponseCode());
    }

    @Test
    void testUser_success() {

        User mockUser = User.builder().id(1).username(username).password(password).email(email).status(1).build();

        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(mockUser));

        // Act
        ResponseEntity<RestApiResponse> response = userService.getUser(userId, refId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseConstant.GENERIC_REQUEST_SUCCESS, response.getBody().getResponseCode());
    }

    @Test
    void testUpdateUser_success() {

        User mockUser = User.builder().id(1).username(username).password(password).email(email).status(1).build();

        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(mockUser));

        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder().email(email).build();

        // Act
        ResponseEntity<RestApiResponse> response = userService.updateUser(userId, userUpdateDTO, refId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseConstant.GENERIC_REQUEST_SUCCESS, response.getBody().getResponseCode());
    }

    @Test
    void testDeleteUser_success() {

        User mockUser = User.builder().id(1).username(username).password(password).email(email).status(1).build();

        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(mockUser));

        // Act
        ResponseEntity<RestApiResponse> response = userService.deleteUser(userId, refId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseConstant.GENERIC_REQUEST_SUCCESS, response.getBody().getResponseCode());
    }

}
