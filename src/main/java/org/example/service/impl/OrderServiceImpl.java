package org.example.service.impl;


import org.example.constant.ResponseConstant;
import org.example.dto.CommonException;
import org.example.dto.RestApiResponse;
import org.example.dto.request.OrderDTO;
import org.example.dto.request.UpdateOrderDTO;
import org.example.dto.response.OrderDetailDTO;
import org.example.dto.response.UserDTO;
import org.example.enums.OrderStatusEnum;
import org.example.model.Order;
import org.example.repository.OrderRepository;
import org.example.service.OrderService;
import org.example.service.impl.client.UserServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserServiceClient<UserDTO> userServiceClient;

    @Override
    public ResponseEntity<RestApiResponse> createOrder(OrderDTO order, String refId) {
        log.info("Order creation flow started for order {} for refId {}", order.toString(), refId);
        validateOrderRequest(order, refId);

        UserDTO userDTO = userServiceClient.getUserById(order.getUserId(), UserDTO.class, refId);
        return Optional.ofNullable(userDTO).map(user-> {

            orderRepository.save(Order.builder()
                    .productId(order.getProductId())
                    .quantity(order.getQuantity())
                    .price(order.getPrice())
                    .createdBy(order.getUserId())
                    .status(OrderStatusEnum.PLACED.getOrderStatus())
                    .build());
            log.info("Order creation flow is successful refId {}", refId);
            return new ResponseEntity<>(RestApiResponse.builder().responseCode(ResponseConstant.GENERIC_REQUEST_SUCCESS)
                    .responseMessage(messageSource.getMessage(ResponseConstant.GENERIC_REQUEST_SUCCESS, null, null))
                    .build(), HttpStatus.OK);
        }).orElseThrow(()->
                CommonException.builder().refId(refId)
                .code(ResponseConstant.ERR_DATA_NOT_FOUND)
                .exception(String.format(messageSource.getMessage(ResponseConstant.ERR_DATA_NOT_FOUND, null, null), "User"))
                .build());
    }

    @Override
    public ResponseEntity<RestApiResponse> retrieveOrder(Integer orderId, String refId) {
        log.info("retrieveOrder flow started for orderId {} for refId {}", orderId, refId);

        Optional.ofNullable(orderId).orElseThrow(()-> {
            log.error("Invalid orderId {} for refId {}", orderId, refId);
            return CommonException.builder().refId(refId)
                .code(ResponseConstant.ERR_INVALID_INPUT)
                .exception(String.format(messageSource.getMessage(ResponseConstant.ERR_INVALID_INPUT, null, null), "OrderId"))
                .build();
        });

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        return orderOptional.map(order -> {
            log.info("retrieveOrder flow is successful refId {}", refId);
            return new ResponseEntity<>(RestApiResponse.builder().responseCode(ResponseConstant.GENERIC_REQUEST_SUCCESS)
                    .responseMessage(messageSource.getMessage(ResponseConstant.GENERIC_REQUEST_SUCCESS, null, null))
                    .data(OrderDetailDTO.builder()
                            .orderId(order.getOrderId())
                            .productId(order.getProductId())
                            .userId(order.getCreatedBy())
                            .price(order.getPrice())
                            .quantity(order.getQuantity())
                            .build())
                    .build(), HttpStatus.OK);
        }).orElseThrow(() -> {
            log.error("retrieveOrder Order not found for refId {}", refId);
            return CommonException.builder().refId(refId)
                    .code(ResponseConstant.ERR_DATA_NOT_FOUND)
                    .exception(String.format(messageSource.getMessage(ResponseConstant.ERR_DATA_NOT_FOUND, null, null), "Order"))
                    .build();
        });
    }

    @Override
    public ResponseEntity<RestApiResponse> updateOrder(Integer orderId, UpdateOrderDTO updateOrderDTO, String refId) {
        log.info("updateOrder flow started orderId {} with order {} for refId {}", orderId, updateOrderDTO.toString(), refId);
        validateOrder(updateOrderDTO, refId);

        Optional.ofNullable(orderId).orElseThrow(()-> {
            log.error("Invalid orderId {} for refId {}", orderId, refId);
            return CommonException.builder().refId(refId)
                    .code(ResponseConstant.ERR_INVALID_INPUT)
                    .exception(String.format(messageSource.getMessage(ResponseConstant.ERR_INVALID_INPUT, null, null), "OrderId"))
                    .build();
        });

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        return orderOptional.map(order -> {
            order.setQuantity(updateOrderDTO.getQuantity());
            orderRepository.save(order);
            log.info("updateOrder flow is successful refId {}", refId);
            return new ResponseEntity<>(RestApiResponse.builder().responseCode(ResponseConstant.GENERIC_REQUEST_SUCCESS)
                    .responseMessage(messageSource.getMessage(ResponseConstant.GENERIC_REQUEST_SUCCESS, null, null))
                    .build(), HttpStatus.OK);
        }).orElseThrow(() -> {
            log.error("updateOrder Order not found for refId {}", refId);
            return CommonException.builder().refId(refId)
                    .code(ResponseConstant.ERR_DATA_NOT_FOUND)
                    .exception(String.format(messageSource.getMessage(ResponseConstant.ERR_DATA_NOT_FOUND, null, null), "Order"))
                    .build();
        });
    }

    @Override
    public ResponseEntity<RestApiResponse> deleteOrder(Integer orderId, String refId) {
        log.info("deleteOrder flow started orderId {} for refId {}", orderId, refId);

        Optional.ofNullable(orderId).orElseThrow(()-> {
            log.error("Invalid orderId {} for refId {}", orderId, refId);
            return CommonException.builder().refId(refId)
                    .code(ResponseConstant.ERR_INVALID_INPUT)
                    .exception(String.format(messageSource.getMessage(ResponseConstant.ERR_INVALID_INPUT, null, null), "OrderId"))
                    .build();
        });

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        return orderOptional.map(order -> {
            orderRepository.delete(order);
            log.info("deleteOrder flow is successful refId {}", refId);
            return new ResponseEntity<>(RestApiResponse.builder().responseCode(ResponseConstant.GENERIC_REQUEST_SUCCESS)
                    .responseMessage(messageSource.getMessage(ResponseConstant.GENERIC_REQUEST_SUCCESS, null, null))
                    .build(), HttpStatus.OK);
        }).orElseThrow(() -> {
            log.error("deleteOrder Order not found for refId {}", refId);
            return CommonException.builder().refId(refId)
                    .code(ResponseConstant.ERR_DATA_NOT_FOUND)
                    .exception(String.format(messageSource.getMessage(ResponseConstant.ERR_DATA_NOT_FOUND, null, null), "Order"))
                    .build();
        });
    }

    private void validateOrderRequest(OrderDTO orderDTO, String refId){

        if (null == orderDTO.getPrice() || orderDTO.getPrice()<=0 ) {
            log.error("Order price not found for refId {}", refId);
            throw CommonException.builder().code(ResponseConstant.ERR_DATA_NOT_FOUND)
                    .exception(String.format(messageSource.getMessage(ResponseConstant.ERR_DATA_NOT_FOUND, null, null), "Price"))
                    .refId(refId).build();
        }

        if(null == orderDTO.getProductId() || orderDTO.getProductId()<=0 ){
            log.error("Product not found for refId {}", refId);
            throw CommonException.builder().code(ResponseConstant.ERR_DATA_NOT_FOUND)
                    .exception(String.format(messageSource.getMessage(ResponseConstant.ERR_DATA_NOT_FOUND, null, null), "ProductId"))
                    .refId(refId).build();
        }

        if(null == orderDTO.getQuantity() || orderDTO.getQuantity()<=0 ){
            log.error("Order quantity not found for refId {}", refId);
            throw CommonException.builder().code(ResponseConstant.ERR_DATA_NOT_FOUND)
                    .exception(String.format(messageSource.getMessage(ResponseConstant.ERR_DATA_NOT_FOUND, null, null), "ProductId"))
                    .refId(refId).build();
        }
    }

    void validateOrder(UpdateOrderDTO updateOrderDTO, String refId){
        if(null == updateOrderDTO.getQuantity() || updateOrderDTO.getQuantity()<=0 ){
            log.error("Order quantity not found for refId {}", refId);
            throw CommonException.builder().code(ResponseConstant.ERR_DATA_NOT_FOUND)
                    .exception(String.format(messageSource.getMessage(ResponseConstant.ERR_DATA_NOT_FOUND, null, null), "ProductId"))
                    .refId(refId).build();
        }
    }
}
