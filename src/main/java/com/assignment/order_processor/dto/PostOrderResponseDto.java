package com.assignment.order_processor.dto;

import java.util.UUID;

public record PostOrderResponseDto(
        UUID orderId,
        OrderStatus status) {

}
