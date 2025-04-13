package org.example.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id", length = 100)
    private Long orderId;
    @Column(name="product_id", length = 100)
    private Long productId;
    @Column(name="quantity", length = 100)
    private Integer quantity;
    private Double price;
    private int status;
    @Column(name="created_by")
    private Integer createdBy;
    @Column(name="modified_by")
    private Integer modifiedBy;
}
