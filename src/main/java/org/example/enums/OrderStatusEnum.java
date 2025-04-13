package org.example.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum OrderStatusEnum {
    SAVED(0),
    PLACED(1),
    DELIVERED(2),
    PARTIALLY_DELIVERED(3);

    private final Integer orderStatus;

    OrderStatusEnum(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getValue(){
        return orderStatus;
    }

    public static OrderStatusEnum fromValue(Integer value){
        for (OrderStatusEnum orderStatusEnum: OrderStatusEnum.values()){
            if (Objects.equals(orderStatusEnum.orderStatus, value)){
                return orderStatusEnum;
            }
        }
        throw new IllegalArgumentException("Invalid status:" + value);
    }
}
