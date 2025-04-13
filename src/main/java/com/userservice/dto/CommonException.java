package com.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class CommonException extends RuntimeException{
    private String code;
    private String exception;
    private String refId;
}
