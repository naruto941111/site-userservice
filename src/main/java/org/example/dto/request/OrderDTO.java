package org.example.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer userId;
    private Long productId;
    private Integer quantity;
    private Double price;
}
