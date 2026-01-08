package com.assignment.order_processor.exceptions;


import com.assignment.order_processor.exceptions.customException.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<String> handleNotValidMethodArgs(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getLocalizedMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    protected ResponseEntity<String> handleOrderNotFound(OrderNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
