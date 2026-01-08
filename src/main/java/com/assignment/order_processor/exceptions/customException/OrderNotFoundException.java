package com.assignment.order_processor.exceptions.customException;

public class OrderNotFoundException extends RuntimeException  {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
