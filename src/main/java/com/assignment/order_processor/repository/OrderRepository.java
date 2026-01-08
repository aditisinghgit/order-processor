package com.assignment.order_processor.repository;

import com.assignment.order_processor.entity.Order;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends CrudRepository<Order, UUID> {
    public Optional<List<Order>> findByCustomerId(String customerId);
    public Optional<List<Order>> findByUsername(String username);
}
