package org.example.controller;

import org.example.dto.RestApiResponse;
import org.example.dto.request.OrderDTO;
import org.example.dto.request.UpdateOrderDTO;
import org.example.service.OrderService;
import org.example.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderServiceController {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceController.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private Utility utility;

    @PostMapping(value = "/orders")
    public ResponseEntity<RestApiResponse> createOrder(@RequestBody OrderDTO orderDTO) {
        String refId = utility.generateUUID("ORDER");
        log.info("Create order flow started {} for refId {}", orderDTO.toString(), refId);
        return orderService.createOrder(orderDTO, refId);
    }

    @GetMapping(value = "/orders/{id}")
    public ResponseEntity<RestApiResponse> userLogin(@PathVariable Integer id) {
        String refId = utility.generateUUID("GET_ORDER");
        log.info("Get order flow started {} for refId {}", id, refId);
        return orderService.retrieveOrder(id, refId);
    }

    @PutMapping(value = "/orders/{id}")
    public Object updateOrder(@PathVariable Integer id, @RequestBody UpdateOrderDTO updateOrderDTO) {
        String refId = utility.generateUUID("UPDATE_ORDER");
        log.info("Update order flow started {} for refId {}", updateOrderDTO.toString(), refId);
        return orderService.updateOrder(id, updateOrderDTO, refId);
    }

    @DeleteMapping(value = "/orders/{id}")
    public ResponseEntity<RestApiResponse> deleteOrder(@PathVariable Integer id) {
        String refId = utility.generateUUID("DEL_ORDER");
        log.info("Delete order flow started for refId {}", refId);
        return orderService.deleteOrder(id, refId);
    }
}
