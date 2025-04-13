package com.userservice.dto;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@NoArgsConstructor
@FieldNameConstants
@Getter
@Setter
@AllArgsConstructor(staticName = "create")
public class UserPrincipal {
    private UserDetails userDetails;
}
