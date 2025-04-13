package com.userservice.controller;

import com.userservice.dto.request.UserRegistrationDTO;
import com.userservice.dto.request.UserUpdateDTO;
import com.userservice.dto.response.RestApiResponse;
import com.userservice.dto.request.UserLoginDTO;
import com.userservice.service.UserService;
import com.userservice.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserServiceController {
    private static final Logger log = LoggerFactory.getLogger(UserServiceController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private Utility utility;

    @PostMapping(value = "/users/login/{username}")
    public ResponseEntity<RestApiResponse> userLogin(@PathVariable String username, @RequestBody UserLoginDTO userLoginDTO) {
        String refId = utility.generateUUID("USLOGIN");
        log.info("User login flow started {} for refId {}", userLoginDTO.toString(), refId);
        return userService.userLogin(username, userLoginDTO, refId);
    }

    @PostMapping(value = "/users")
    public Object userLogin(@RequestBody UserRegistrationDTO userRegistrationDto) {
        String refId = utility.generateUUID("USERREG");
        log.info("User registration flow started {} for refId {}", userRegistrationDto.toString(), refId);
        return userService.userRegistration(userRegistrationDto, refId);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<RestApiResponse> userLogin(@PathVariable Integer id) {
        String refId = utility.generateUUID("GET_USER");
        log.info("Get User flow started {} for refId {}", id, refId);
        return userService.getUser(id, refId);
    }

    @PutMapping(value = "/users/{id}")
    public ResponseEntity<RestApiResponse> userLogin(@PathVariable Integer id, @RequestBody UserUpdateDTO userUpdateDTO) {
        String refId = utility.generateUUID("USERREG");
        log.info("User registration flow started {} for refId {}", userUpdateDTO.toString(), refId);
        return userService.updateUser(id, userUpdateDTO, refId);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<RestApiResponse> deleteUser(@PathVariable Integer id) {
        String refId = utility.generateUUID("USERREG");
        log.info("User delete flow started for refId {}", refId);
        return userService.deleteUser(id, refId);
    }
}
