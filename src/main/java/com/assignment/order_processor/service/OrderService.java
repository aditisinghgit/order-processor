package com.assignment.order_processor.service;

import com.assignment.order_processor.dto.OrderStatus;
import com.assignment.order_processor.dto.PostOrderRequestDto;
import com.assignment.order_processor.entity.Order;
import com.assignment.order_processor.exceptions.customException.OrderNotFoundException;
import com.assignment.order_processor.repository.OrderRepository;
import com.assignment.order_processor.utility.OrderFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    OrderFileWriter fileWriter;

    public Order createOrder(PostOrderRequestDto request, Authentication authentication) {

        logger.info(
                "Creating order | CustomerId={} | Product={} | Amount={}",
                request.customerId(),
                request.product(),
                request.price()
        );

        Order order = new Order();
        order.setCustomerId(request.customerId());
        order.setProduct(request.product());
        order.setPrice(request.price());
        order.setUsername(authentication.getName());
        order.setStatus(OrderStatus.CREATED);

        orderRepo.save(order);

        logger.info(
                "Order saved to database | OrderId={} | Status={}",
                order.getOrderId(),
                order.getStatus()
        );

        fileWriter.writeOrderToFile(order);

        logger.info(
                "Order written to file | OrderId={}",
                order.getOrderId()
        );

        return order;
    }

    public Order retrieveOrder(UUID orderId){

        logger.info(
                "Retrieving order | OrderId={}",
                orderId
        );

        return orderRepo.findById(orderId)
                .orElseThrow(() -> {
                    logger.error(
                            "Order not found | OrderId={}",
                            orderId
                    );
                    return new OrderNotFoundException("Order with order id" + orderId + "not found");
                });
    }

    public List<Order> retrieveOrderByCustomer(String customerId){

        logger.info(
                "Retrieving orders by customer | CustomerId={}",
                customerId
        );

        List<Order> orders = orderRepo.findByCustomerId(customerId).get();

        logger.info(
                "Orders retrieved for customer | CustomerId={} | Count={}",
                customerId,
                orders.size()
        );

        return orders;
    }

    public List<Order> retrieveOrderByUser(Authentication authentication){

        logger.info(
                "Retrieving orders for user | CustomerId={}",
                authentication.getName()
        );

        List<Order> orders = orderRepo.findByUsername(authentication.getName()).get();

        logger.info(
                "Orders retrieved for user | Name={} | Count={}",
                authentication.getName(),
                orders.size()
        );

        return orders;
    }

    public List<Order> retrieveAllOrder(){

        logger.info(
                "Retrieving all orders"
        );

        List<Order> orders = (List<Order>) orderRepo.findAll();

        logger.info(
                "All orders retrieved "
        );

        return orders;
    }

}
