package com.assignment.order_processor.controller;

import com.assignment.order_processor.dto.GetOrderResponseDto;
import com.assignment.order_processor.dto.PostOrderRequestDto;
import com.assignment.order_processor.dto.PostOrderResponseDto;
import com.assignment.order_processor.entity.Order;
import com.assignment.order_processor.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    //Handler for creating order
    @PostMapping
    public ResponseEntity<PostOrderResponseDto> createOrder(@Valid @RequestBody PostOrderRequestDto request, Authentication authentication){
        logger.info("Create order request received | CustomerId={} | Product={} | Amount={}",
                request.customerId(),
                request.product(),
                request.price()
        );
        Order order = orderService.createOrder(request, authentication);

        logger.info("Order created successfully | OrderId={} | Status={}",
                order.getOrderId(),
                order.getStatus()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PostOrderResponseDto(order.getOrderId(), order.getStatus()));
    }

    //Handler for retrieving order by order Id order
    @GetMapping("/{orderId}")
    public ResponseEntity<GetOrderResponseDto> retrieveOrder(@PathVariable UUID orderId){
        logger.info("Retrieve order request received | OrderId={}", orderId);

        Order order = orderService.retrieveOrder(orderId);

        logger.info("Order retrieved successfully | OrderId={} | Status={}",
                order.getOrderId(),
                order.getStatus()
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetOrderResponseDto(
                        order.getOrderId(),
                        order.getCustomerId(),
                        order.getProduct(),
                        order.getPrice(),
                        order.getStatus()
                ));
    }

    //Handler for retrieving order by customer id
    @GetMapping
    public ResponseEntity<List<GetOrderResponseDto>> retrieveOrderByCustomer(@RequestParam String customerId){
        logger.info("Retrieve orders by customer request received | CustomerId={}", customerId);

        List<Order> orders = orderService.retrieveOrderByCustomer(customerId);

        logger.info("Orders retrieved for customer | CustomerId={} | Count={}",
                customerId,
                orders.size()
        );

        //mapping to response DTO
        List<GetOrderResponseDto> response = orders.stream()
                .map(order -> new GetOrderResponseDto(
                        order.getOrderId(),
                        order.getCustomerId(),
                        order.getProduct(),
                        order.getPrice(),
                        order.getStatus()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //Handler for retrieving all order [ADMIN only]
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<GetOrderResponseDto>> getAllOrders() {
        List<Order> orders = orderService.retrieveAllOrder();

        //mapping to response DTO
        List<GetOrderResponseDto> response = orders.stream()
                .map(order -> new GetOrderResponseDto(
                        order.getOrderId(),
                        order.getCustomerId(),
                        order.getProduct(),
                        order.getPrice(),
                        order.getStatus()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //Handler for retrieving order by logged User
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<List<GetOrderResponseDto>> getMyOrders(Authentication auth) {
        List<Order> orders = orderService.retrieveOrderByUser(auth);

        //mapping to response DTO
        List<GetOrderResponseDto> response = orders.stream()
                .map(order -> new GetOrderResponseDto(
                        order.getOrderId(),
                        order.getCustomerId(),
                        order.getProduct(),
                        order.getPrice(),
                        order.getStatus()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
