package com.assignment.order_processor.dto ;

import java.util.UUID ;

public record GetOrderResponseDto(
        UUID orderId ,
        String customerId ,
        String product ,
        Double price ,
        OrderStatus status ) {}
