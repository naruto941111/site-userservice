package com.userservice.service;


import com.userservice.dto.request.UserUpdateDTO;
import com.userservice.dto.response.RestApiResponse;
import com.userservice.dto.request.UserLoginDTO;
import com.userservice.dto.request.UserRegistrationDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<RestApiResponse> userLogin(String username, UserLoginDTO userLoginDTO, String refId);
    Object userRegistration(UserRegistrationDTO userDto, String refId);
    ResponseEntity<RestApiResponse> getUser(Integer userId, String refId);

    ResponseEntity<RestApiResponse> updateUser(Integer id, UserUpdateDTO userDto, String refId);
    ResponseEntity<RestApiResponse> deleteUser(Integer id, String refId);
}
