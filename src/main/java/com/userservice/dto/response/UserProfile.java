package com.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@FieldNameConstants
@AllArgsConstructor(staticName = "create")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfile {
    private String username;
    private String email;
    private Integer status;
    private Date createdAt;
}
