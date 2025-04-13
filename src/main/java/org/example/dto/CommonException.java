package org.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonException extends RuntimeException{
    private String code;
    private String exception;
    private String refId;
}
