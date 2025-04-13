package com.userservice.dto.request;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    private String username;
    private String password;
    private String confirmPassword;
    private String emailId;
}
