package com.assignment.order_processor.apache;

import com.assignment.order_processor.entity.Order;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileToQueueRoute extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(FileToQueueRoute.class);

    @Override
    public void configure() {

        onException(Exception.class)
                .process(exchange -> {
                    Exception exception = exchange.getProperty(Exception.class.getName(), Exception.class);
                    String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
                    logger.error("Error processing file " + fileName + " reason " + exception.getMessage());
                })
                .handled(true)
                .to("file:error/orders");

        from("file:input/orders?move=../../processed/orders")
                .routeId("file-to-order-queue")

                .process(exchange -> {
                    String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
                    logger.info("File received " + fileName);
                })

                .unmarshal().json(JsonLibrary.Jackson, Order.class)

                .process(exchange -> {
                    Order order = exchange.getIn().getBody(Order.class);

                    if (order.getOrderId() == null) {
                        throw new IllegalArgumentException("orderId is null");
                    }

                    if (order.getCustomerId() == null) {
                        throw new IllegalArgumentException("customerId is null");
                    }

                    if (order.getPrice() <= 0) {
                        throw new IllegalArgumentException("amount must be greater than zero");
                    }

                    logger.info("Valid order with orderId " + order.getOrderId());
                })

                .marshal().json(JsonLibrary.Jackson)

                .to("spring-rabbitmq:ORDER.CREATED.QUEUE")

                .process(exchange -> {
                    logger.info("Order sent to queue");
                });
    }
}
