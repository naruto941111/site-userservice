package org.example.dto.response;

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
public class OrderDetailDTO {
    private Long orderId;
    private Long productId;
    private Integer userId;
    private Integer quantity;
    private Double price;
}
