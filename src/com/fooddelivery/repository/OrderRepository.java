package com.fooddelivery.repository;

import com.fooddelivery.model.Order;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OrderRepository {
    private final Map<String, Order> orderById = new HashMap<>();

    public void save(Order order) {
        orderById.put(order.getId(), order);
    }

    public Optional<Order> findById(String id) {
        return Optional.ofNullable(orderById.get(id));
    }

    public Collection<Order> findAll() {
        return orderById.values();
    }
}
