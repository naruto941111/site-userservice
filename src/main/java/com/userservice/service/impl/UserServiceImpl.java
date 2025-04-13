package com.userservice.service.impl;

import com.userservice.constant.ResponseConstant;
import com.userservice.dto.*;
import com.userservice.dto.request.UserLoginDTO;
import com.userservice.dto.request.UserRegistrationDTO;
import com.userservice.dto.request.UserUpdateDTO;
import com.userservice.dto.response.RestApiResponse;
import com.userservice.dto.response.UserProfile;
import com.userservice.model.User;
import com.userservice.repository.UserRepository;
import com.userservice.service.UserService;
import com.userservice.service.ValidationService;
import com.userservice.utility.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private ValidationService validationService;

    @Override
    public ResponseEntity<RestApiResponse> userLogin(String username, UserLoginDTO userLoginRequestDTO, String refId) {
        try {
            loginVerification(username, userLoginRequestDTO, refId);
            User user = getLoggedInUser(username, userLoginRequestDTO, refId);

            String token = jwtTokenProvider.generateJWT(username);
            user.setToken(token);
            userRepository.save(user);

            log.info("Login request completed for refId {}", refId);
            return new ResponseEntity<>(RestApiResponse.builder().responseCode(ResponseConstant.GENERIC_REQUEST_SUCCESS)
                    .data(token)
                    .refId(refId).build(), HttpStatus.OK);
        }
        catch (Exception ex){
            log.error("User login error occurred {} for refId {}", ex.getMessage(), refId);

        }
        return new ResponseEntity<>(RestApiResponse.builder().responseCode(ResponseConstant.USER_LOGIN_FAILED)
                .refId(refId).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Object userRegistration(UserRegistrationDTO userDto, String refId) {
        validateRegistrationRequest(userDto, refId);

        try {
            Optional<User> userOptional = userRepository.findByUsername(userDto.getUsername());
            return userOptional.map(user-> {
                        log.error("User already exists for refId {}", refId);
                        throw CommonException.builder().code(ResponseConstant.USER_EXISTS)
                                .exception(messageSource.getMessage(ResponseConstant.USER_EXISTS, null, null))
                                .refId(refId).build();

                    }).orElseGet(()-> {
                        userRepository.save(User.builder().username(userDto.getUsername())
                                .email(userDto.getEmailId())
                                .status(1)
                                .password(passwordEncoder.encode(userDto.getPassword())).build());
                        log.info("User registration  is successful refId {}", refId);
                        return new ResponseEntity<>(RestApiResponse.builder().responseCode(ResponseConstant.USER_REGISTERED_SUCCESSFUL)
                        .responseMessage(messageSource.getMessage(ResponseConstant.USER_REGISTERED_SUCCESSFUL, null, null))
                        .refId(refId)
                        .build(), HttpStatus.OK);
                    });
        }
        catch (Exception ex){
            log.error("User registration failed for refId {}", refId);
            if (ex instanceof CommonException commonException) {
                throw commonException;
            }
            throw CommonException.builder().refId(refId)
                    .code(ResponseConstant.SERVER_GENERIC_EXCEPTION_CODE)
                    .exception(messageSource.getMessage(ResponseConstant.SERVER_GENERIC_EXCEPTION_CODE, null, null))
                    .build();
        }
    }

    @Override
    public ResponseEntity<RestApiResponse> getUser(Integer userId, String refId) {
        try {
            Optional<User> updateUser = userRepository.findById(userId);
            return updateUser.map(user-> {
                        log.info("User detail fetched for refId {}", refId);

                        return new ResponseEntity<>(RestApiResponse.builder().responseCode(ResponseConstant.GENERIC_REQUEST_SUCCESS)
                                        .responseMessage(messageSource.getMessage(ResponseConstant.GENERIC_REQUEST_SUCCESS, null, null))
                                        .data(UserProfile.builder()
                                        .email(user.getEmail())
                                        .status(user.getStatus())
                                        .username(user.getUsername())
                                        .createdAt(user.getCreatedAt())
                                        .build())
                                .refId(refId)
                                .build(), HttpStatus.OK);
                    }).orElseThrow(()->
                                        CommonException.builder()
                                        .refId(refId)
                                        .code(ResponseConstant.USERNAME_NOT_FOUND)
                                        .exception(messageSource.getMessage(ResponseConstant.USERNAME_NOT_FOUND, null, null))
                                        .build());
        }
        catch (Exception ex){
            log.error("Error occurred while fetching user data for refId {}", refId);
            if (ex instanceof CommonException commonException) {
                throw commonException;
            }
            throw CommonException.builder()
                    .refId(refId)
                    .code(ResponseConstant.SERVER_GENERIC_EXCEPTION_CODE)
                    .exception(messageSource.getMessage(ResponseConstant.SERVER_GENERIC_EXCEPTION_CODE, null, null))
                    .build();
        }

    }

    @Override
    public ResponseEntity<RestApiResponse> updateUser(Integer userId, UserUpdateDTO userUpdateDTO, String refId) {

        validateUpdateUserDetail(userUpdateDTO, refId);

        try {
            Optional<User> updateUser = userRepository.findById(userId);
            return updateUser.map(user-> {

                log.info("User updated for refId {}", refId);
                updateUser.get().setEmail(userUpdateDTO.getEmail());
                userRepository.save(updateUser.get());

                return new ResponseEntity<>(RestApiResponse.builder().responseCode(ResponseConstant.GENERIC_REQUEST_SUCCESS)
                        .responseMessage(messageSource.getMessage(ResponseConstant.GENERIC_REQUEST_SUCCESS, null, null))
                        .build(), HttpStatus.OK);
            }).orElseThrow(()-> CommonException.builder()
                    .refId(refId)
                    .code(ResponseConstant.USERNAME_NOT_FOUND)
                    .exception(messageSource.getMessage(ResponseConstant.USERNAME_NOT_FOUND, null, null))
                    .build());
        }
        catch (Exception ex){
            log.error("Error occurred while fetching user data for refId {}", refId);
            if (ex instanceof CommonException commonException) {
                throw commonException;
            }
            throw CommonException.builder()
                    .refId(refId)
                    .code(ResponseConstant.SERVER_GENERIC_EXCEPTION_CODE)
                    .exception(messageSource.getMessage(ResponseConstant.SERVER_GENERIC_EXCEPTION_CODE, null, null))
                    .build();
        }
    }

    @Override
    public ResponseEntity<RestApiResponse> deleteUser(Integer userId, String refId) {

        try {
            Optional<User> updateUser = userRepository.findById(userId);
            return updateUser.map(user-> {

                log.info("User updated for refId {}", refId);
                userRepository.delete(updateUser.get());

                return new ResponseEntity<>(RestApiResponse.builder().responseCode(ResponseConstant.GENERIC_REQUEST_SUCCESS)
                        .responseMessage(messageSource.getMessage(ResponseConstant.GENERIC_REQUEST_SUCCESS, null, null))
                        .build(), HttpStatus.OK);
            }).orElseThrow(()-> CommonException.builder()
                    .refId(refId)
                    .code(ResponseConstant.USERNAME_NOT_FOUND)
                    .exception(messageSource.getMessage(ResponseConstant.USERNAME_NOT_FOUND, null, null))
                    .build());
        }
        catch (Exception ex){
            log.error("Error occurred while updating user data for refId {}", refId);
            if (ex instanceof CommonException commonException) {
                throw commonException;
            }
            throw CommonException.builder()
                    .refId(refId)
                    .code(ResponseConstant.SERVER_GENERIC_EXCEPTION_CODE)
                    .exception(messageSource.getMessage(ResponseConstant.SERVER_GENERIC_EXCEPTION_CODE, null, null))
                    .build();
        }
    }

    public void loginVerification(String username, UserLoginDTO userLoginDTO, String refId) {
        if(Objects.isNull(userLoginDTO)){
            log.error("Invalid request for refId {}", refId);
            throw CommonException.builder().code(ResponseConstant.USER_LOGIN_FAILED)
                    .exception(messageSource.getMessage(ResponseConstant.USER_LOGIN_FAILED, null, null))
                    .refId(refId).build();
        }

        if(null == username || username.trim().isEmpty()){
            log.error("Username is required but not found for refId {}", refId);
            throw CommonException.builder().code(ResponseConstant.USERNAME_NOT_FOUND)
                    .exception(messageSource.getMessage(ResponseConstant.USERNAME_NOT_FOUND, null, null))
                    .refId(refId).build();
        }

        if(null == userLoginDTO.getPassword() || userLoginDTO.getPassword().trim().isEmpty()){
            log.error("Password is required but not found for refId {}", refId);
            throw CommonException.builder().code(ResponseConstant.USERNAME_NOT_FOUND)
                    .exception(messageSource.getMessage(ResponseConstant.USERNAME_NOT_FOUND, null, null))
                    .refId(refId).build();
        }

    }

    public User getLoggedInUser(String username, UserLoginDTO userLoginRequestDTO, String refId){
        Optional<User> user = userRepository.findByUsernameAndStatus(username, 1);

        if (user.isEmpty()) {
            log.error("User not found login request for refId {}", refId);
            throw CommonException.builder().code(ResponseConstant.USER_LOGIN_FAILED)
                    .exception(messageSource.getMessage(ResponseConstant.USER_LOGIN_FAILED, null, null))
                    .refId(refId).build();
        }

        if (!checkPassword(userLoginRequestDTO.getPassword(), user.get().getPassword())) {
            log.info("Invalid password user login request for refId {}", refId);
            throw CommonException.builder().code(ResponseConstant.USER_LOGIN_FAILED)
                    .exception(messageSource.getMessage(ResponseConstant.USER_LOGIN_FAILED, null, null))
                    .refId(refId).build();
        }

        return user.get();
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private void validateRegistrationRequest(UserRegistrationDTO userDto, String refId) {
        if (null == userDto || null == userDto.getUsername() || userDto.getUsername().trim().isEmpty()) {
            log.error("User registration username not found for refId {}", refId);
            throw new CommonException(
                    ResponseConstant.USERNAME_NOT_FOUND,
                    messageSource.getMessage(ResponseConstant.USERNAME_NOT_FOUND, null, null),
                    refId);
        }

        if(null == userDto.getPassword() || userDto.getPassword().trim().isEmpty()){
            log.error("User registration password not found for refId {}", refId);
            throw CommonException.builder().code(ResponseConstant.PASSWORD_NOT_FOUND)
                    .exception(messageSource.getMessage(ResponseConstant.PASSWORD_NOT_FOUND, null, null))
                    .refId(refId).build();
        }

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw CommonException.builder().code(ResponseConstant.PASS_MISMATCH)
                    .exception(messageSource.getMessage(ResponseConstant.PASS_MISMATCH, null, null))
                    .refId(refId).build();
        }

        if (!userDto.getUsername().matches("^[a-zA-Z0-9@#._-]{1,10}$")) {
            throw CommonException.builder().code(ResponseConstant.USERNAME_VALIDATION)
                    .exception(messageSource.getMessage(ResponseConstant.USERNAME_VALIDATION, null, null))
                    .refId(refId).build();
        }

        validationService.emailIdVerification(userDto.getEmailId(), refId);
    }

    private void validateUpdateUserDetail(UserUpdateDTO userUpdateDTO, String refId){
        validationService.emailIdVerification(userUpdateDTO.getEmail(), refId);
    }
}
