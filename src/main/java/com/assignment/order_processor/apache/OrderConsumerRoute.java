package com.assignment.order_processor.apache;

import com.assignment.order_processor.entity.Order;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumerRoute extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumerRoute.class);

    @Override
    public void configure() {

        from("spring-rabbitmq:ORDER.CREATED.QUEUE")
                .routeId("order-consumer")

                // JSON → Order
                .unmarshal().json(JsonLibrary.Jackson, Order.class)

                // Log details
                .process(exchange -> {
                    Order order = exchange.getIn().getBody(Order.class);

                    logger.info(
                            "Order processed | OrderId={} | CustomerId={} | Amount={}",
                            order.getOrderId(),
                            order.getCustomerId(),
                            order.getPrice()
                    );
                });
    }
}
