package org.example.service;

import org.example.dto.RestApiResponse;
import org.example.dto.request.OrderDTO;
import org.example.dto.request.UpdateOrderDTO;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<RestApiResponse> createOrder(OrderDTO order, String refId);
    ResponseEntity<RestApiResponse> retrieveOrder(Integer orderId, String refId);
    ResponseEntity<RestApiResponse> updateOrder(Integer orderId, UpdateOrderDTO updateOrderDTO, String refId);
    ResponseEntity<RestApiResponse> deleteOrder(Integer orderId, String refId);
}
