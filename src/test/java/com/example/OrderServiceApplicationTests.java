package com.example;

import org.example.OrderServiceApplication;
import org.example.constant.ResponseConstant;
import org.example.dto.CommonException;
import org.example.dto.RestApiResponse;
import org.example.dto.request.OrderDTO;
import org.example.dto.request.UpdateOrderDTO;
import org.example.dto.response.UserDTO;
import org.example.model.Order;
import org.example.repository.OrderRepository;
import org.example.service.impl.OrderServiceImpl;
import org.example.service.impl.client.UserServiceClient;
import org.example.utility.Utility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OrderServiceApplication.class)
@AutoConfigureMockMvc
class OrderServiceApplicationTests {

    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Logger log;

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private Utility utility;
    @Mock
    private UserServiceClient<UserDTO> userServiceClient;

    private Integer userId = 1;
    private String username = "admin123";
    private String email = "admin@dummy.com";
    private String refId = "ref123";

    @Test
    void testCreateOrder_success() {

       UserDTO mockUser = UserDTO.builder().username(username)
                .email(email)
                .status(1)
                .createdAt(new Date())
                .build();

        OrderDTO orderDTO = OrderDTO.builder()
                        .price(10.0)
                        .userId(1)
                        .quantity(7)
                        .productId(2L)
                        .build();

        when(userServiceClient.getUserById(1, UserDTO.class, refId)).thenReturn(mockUser);

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
//      when(messageSource.getMessage(ResponseConstant.GENERIC_REQUEST_SUCCESS, any(), any())).thenReturn("Request completed successfully");

        // Act
        ResponseEntity<RestApiResponse> response = orderService.createOrder(orderDTO, refId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseConstant.GENERIC_REQUEST_SUCCESS, response.getBody().getResponseCode());
    }

    @Test
    void testCreateOrder_fail() {

        when(userServiceClient.getUserById(1, UserDTO.class, refId)).thenReturn(null);

        OrderDTO orderDTO = OrderDTO.builder()
                .userId(1)
                .quantity(7)
                .productId(2L)
                .build();

        //Validation test cases
        when(messageSource.getMessage(any(), any(), any())).thenReturn("%s not found");

        try {
            orderService.createOrder(orderDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_DATA_NOT_FOUND", ex.getCode());
        }

        orderDTO = OrderDTO.builder()
                .price(10.0)
                .userId(1)
                .quantity(7)
                .build();


        try {
            orderService.createOrder(orderDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_DATA_NOT_FOUND", ex.getCode());
        }

        orderDTO = OrderDTO.builder()
                .price(10.0)
                .userId(1)
                .productId(2L)
                .build();


        try {
            orderService.createOrder(orderDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_DATA_NOT_FOUND", ex.getCode());
        }


        orderDTO = OrderDTO.builder()
                .price(10.0)
                .userId(1)
                .quantity(7)
                .productId(2L)
                .build();

        try {
            orderService.createOrder(orderDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_DATA_NOT_FOUND", ex.getCode());
        }
    }

    @Test
    void testRetrieveOrder_success() {

      Order mockOrder =  Order.builder()
            .orderId(1L)
            .productId(2L)
            .quantity(10)
            .price(50.0)
            .status(1)
            .createdBy(1)
            .build();

        when(orderRepository.findById(any())).thenReturn(Optional.of(mockOrder));
        // Act
        ResponseEntity<RestApiResponse> response = orderService.retrieveOrder(1, refId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseConstant.GENERIC_REQUEST_SUCCESS, response.getBody().getResponseCode());
    }

    @Test
    void testRetrieveOrder_failure() {

        //Validation test cases
        when(messageSource.getMessage(any(), any(), any())).thenReturn("%s not found");

        try {
            orderService.retrieveOrder(null, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_INVALID_INPUT", ex.getCode());
        }

        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        try {
            orderService.retrieveOrder(1, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_DATA_NOT_FOUND", ex.getCode());
        }
    }

    @Test
    void testUpdateOrder_success() {

        UpdateOrderDTO updateOrderDTO = UpdateOrderDTO.builder().quantity(10).build();

        Order mockOrder =  Order.builder()
                .orderId(1L)
                .productId(2L)
                .quantity(10)
                .price(50.0)
                .status(1)
                .createdBy(1)
                .build();

        when(orderRepository.findById(any())).thenReturn(Optional.ofNullable(mockOrder));
        ResponseEntity<RestApiResponse> response = orderService.updateOrder(1, updateOrderDTO, refId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseConstant.GENERIC_REQUEST_SUCCESS, response.getBody().getResponseCode());
    }

    @Test
    void testUpdateOrder_failure() {

        UpdateOrderDTO updateOrderDTO = UpdateOrderDTO.builder().build();

        //Validation test cases
        when(messageSource.getMessage(any(), any(), any())).thenReturn("%s not found");

        try {
            orderService.updateOrder(1, updateOrderDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_DATA_NOT_FOUND", ex.getCode());
        }

        try {
            orderService.updateOrder(null, updateOrderDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_DATA_NOT_FOUND", ex.getCode());
        }

        when(orderRepository.findById(any())).thenReturn(Optional.empty());
        try {
            orderService.updateOrder(1, updateOrderDTO, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_DATA_NOT_FOUND", ex.getCode());
        }
    }

    @Test
    void testDeleteOrder_success() {

        Order mockOrder =  Order.builder()
                .orderId(1L)
                .productId(2L)
                .quantity(10)
                .price(50.0)
                .status(1)
                .createdBy(1)
                .build();

        when(orderRepository.findById(any())).thenReturn(Optional.ofNullable(mockOrder));

        //Validation test cases
        when(messageSource.getMessage(any(), any(), any())).thenReturn("%s not found");

        ResponseEntity<RestApiResponse> response= orderService.deleteOrder(1, refId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseConstant.GENERIC_REQUEST_SUCCESS, response.getBody().getResponseCode());
    }

    @Test
    void testDeleteOrder_failure() {

        when(messageSource.getMessage(any(), any(), any())).thenReturn("%s not found");

        try {
            orderService.deleteOrder(null, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_INVALID_INPUT", ex.getCode());
        }

        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        try {
            orderService.deleteOrder(1, refId);
            fail("Expected exception to be thrown");
        } catch (CommonException ex) {
            assertEquals("ERR_DATA_NOT_FOUND", ex.getCode());
        }
    }

}
