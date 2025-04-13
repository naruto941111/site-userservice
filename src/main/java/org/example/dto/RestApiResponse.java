package org.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@Builder
@NoArgsConstructor
@FieldNameConstants
@AllArgsConstructor(staticName = "create")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestApiResponse {
    private String responseCode;
    private String responseMessage;
    private Object data;
    private String refId;
}
