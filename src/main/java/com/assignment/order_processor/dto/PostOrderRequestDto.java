package com.assignment.order_processor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PostOrderRequestDto(

    @NotBlank(message = "Customer Id is required")
    String customerId,

    @NotBlank(message = "Product name is required")
    String product,

    @Positive(message = "Price must be greater than 0")
    Double price

    ){}
