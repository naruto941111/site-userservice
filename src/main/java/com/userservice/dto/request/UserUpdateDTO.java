package com.userservice.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    private String email;
}
