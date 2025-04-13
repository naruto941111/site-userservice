package com.userservice;

import com.userservice.constant.ResponseConstant;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class UserServiceFailureTestApplication {
    @Mock
    private MessageSource messageSource;
    @Mock
    private ValidationService validationService;
    @InjectMocks
    private UserServiceImpl injUserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
    void testUserLogin_username_failure() {

        UserLoginDTO loginDTO = UserLoginDTO.builder().password(password).build();
        // Act
        ResponseEntity<RestApiResponse> response = injUserService.userLogin(null, loginDTO, refId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseConstant.USER_LOGIN_FAILED, response.getBody().getResponseCode());
    }

    @Test
    void testUserLogin_null_failure() {

        // Act
        ResponseEntity<RestApiResponse> response = injUserService.userLogin(username, null, refId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseConstant.USER_LOGIN_FAILED, response.getBody().getResponseCode());
    }

    @Test
    void testUserLogin_password_failure() {
        UserLoginDTO loginDTO = UserLoginDTO.builder().build();
        // Act
        ResponseEntity<RestApiResponse> response = injUserService.userLogin(username, loginDTO, refId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseConstant.USER_LOGIN_FAILED, response.getBody().getResponseCode());
    }

    @Test
    void testUserLogin_nonuser_failure() {

        when(userRepository.findByUsernameAndStatus(username, 1)).thenReturn(Optional.empty());

        UserLoginDTO loginDTO = UserLoginDTO.builder().password(password).build();
        // Act
        ResponseEntity<RestApiResponse> response = injUserService.userLogin(username, loginDTO, refId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseConstant.USER_LOGIN_FAILED, response.getBody().getResponseCode());
    }

    @Test
    void testUserLogin_password_mismatch_failure() {

        User mockUser = User.builder().id(1).username(username).password(encodedPassword).email(email).status(1).build();
        when(userRepository.findByUsernameAndStatus(username, 1)).thenReturn(Optional.ofNullable(mockUser));

        UserLoginDTO loginDTO = UserLoginDTO.builder().password(password).build();
        // Act
        ResponseEntity<RestApiResponse> response = injUserService.userLogin(username, loginDTO, refId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseConstant.USER_LOGIN_FAILED, response.getBody().getResponseCode());
    }

    @Test
    void testUserRegistration_failure() {

        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
                .emailId(email).username(username).password(password).confirmPassword(password).build();

        User mockUser = User.builder().id(1).username(username).password(password).email(email).status(1).build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(mockUser));

        try {
            injUserService.userRegistration(userRegistrationDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_USER_ALREADY_EXISTS", ex.getCode());
        }

        when(userRepository.findByUsername(username))
                .thenThrow(new RuntimeException("DB connection error"));

        try {
            injUserService.userRegistration(userRegistrationDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("REQ_FAILED", ex.getCode());
        }
    }

    @Test
    void testUserRegistration_username_failure() {

        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
                .password(password).confirmPassword(password).emailId(email).build();

//        doThrow(new CommonException("ERR_USERNAME_NOT_FOUND", "Username not found", refId)).when(injUserService).userRegistration(any(), any());

        try {
            injUserService.userRegistration(userRegistrationDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_USERNAME_NOT_FOUND", ex.getCode());
        }
    }

    @Test
    void testUserRegistration_password_failure() {

        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
                .username(username).emailId(email).build();

        try {
            injUserService.userRegistration(userRegistrationDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_PASSWORD_NOT_FOUND", ex.getCode());
        }
    }

    @Test
    void testUserRegistration_password_not_match_failure() {

        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
                .username(username).password(password).confirmPassword(encodedPassword).emailId(email).build();

        try {
            injUserService.userRegistration(userRegistrationDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERPASS001", ex.getCode());
        }
    }

    @Test
    void testUserRegistration_username_validation_failure() {

        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
                .username("&test").password(password).confirmPassword(password).emailId(email).build();

        try {
            injUserService.userRegistration(userRegistrationDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_USERNAME_VALID", ex.getCode());
        }
    }

    @Test
    void testUserRegistration_email_validation_failure() {

        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
                .username(username).password(password).confirmPassword(password).build();

        doThrow(new CommonException("ERR_EMAIL_BLANK", "User email is blank. Please try again.", refId))
                .when(validationService).emailIdVerification(any(), any());

        try {
            injUserService.userRegistration(userRegistrationDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_EMAIL_BLANK", ex.getCode());
        }

        doThrow(new CommonException("ERR_INVALID_EMAIL", "Invalid email. Please try again", refId))
                .when(validationService).emailIdVerification(any(), any());
        userRegistrationDTO = UserRegistrationDTO.builder()
                .username(username).password(password).confirmPassword(password).emailId("dgffy").build();

        try {
            injUserService.userRegistration(userRegistrationDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_INVALID_EMAIL", ex.getCode());
        }
    }

    @Test
    void testGetUser() {

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        try {
            injUserService.getUser(userId, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_USERNAME_NOT_FOUND", ex.getCode());
        }

        when(userRepository.findById(userId)).thenThrow(new RuntimeException("DB connection error"));

        try {
            injUserService.getUser(userId, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("REQ_FAILED", ex.getCode());
        }
    }

    @Test
    void testUpdateUser() {

        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder().build();

        doThrow(new CommonException("ERR_EMAIL_BLANK", "User email is blank. Please try again.", refId)).when(validationService).emailIdVerification(any(), any());

        try {
            injUserService.updateUser(userId, userUpdateDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_EMAIL_BLANK", ex.getCode());
        }

        doThrow(new CommonException("ERR_INVALID_EMAIL", "Invalid email. Please try again", refId)).when(validationService).emailIdVerification(any(), any());

        try {
            injUserService.updateUser(userId, userUpdateDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_INVALID_EMAIL", ex.getCode());
        }

        doNothing().when(validationService).emailIdVerification(any(), any());

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        try {
            injUserService.updateUser(userId, userUpdateDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_USERNAME_NOT_FOUND", ex.getCode());
        }

        when(userRepository.findById(userId)).thenThrow(new RuntimeException("DB connection error"));

        try {
            injUserService.updateUser(userId, userUpdateDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("REQ_FAILED", ex.getCode());
        }
    }

    @Test
    void testDeleteUser() {

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        try {
            injUserService.deleteUser(userId, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_USERNAME_NOT_FOUND", ex.getCode());
        }

        when(userRepository.findById(userId)).thenThrow(new RuntimeException("DB connection error"));

        try {
            injUserService.deleteUser(userId, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("REQ_FAILED", ex.getCode());
        }
    }
}
