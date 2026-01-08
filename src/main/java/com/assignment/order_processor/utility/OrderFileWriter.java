package com.assignment.order_processor.utility;

import com.assignment.order_processor.entity.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class OrderFileWriter {

    private static final Logger logger = LoggerFactory.getLogger(OrderFileWriter.class);

    private final ObjectMapper objectMapper;

    public OrderFileWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void writeOrderToFile(Order order){

        logger.info(
                "Writing order to file | OrderId={}",
                order.getOrderId()
        );

        try {
            String json = objectMapper.writeValueAsString(order);

            Path path = Paths.get(
                    "input/orders/order-" + order.getOrderId() + ".json"
            );

            Files.write(path, json.getBytes());

            logger.info(
                    "Order file written successfully | OrderId={} ",
                    order.getOrderId()
            );

        } catch (Exception e) {

            logger.error(
                    "Failed to write order to file | OrderId={}",
                    order.getOrderId(),
                    e
            );

            throw new RuntimeException(e);
        }
    }
}
