package com.fooddelivery.service;

import com.fooddelivery.model.*;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.repository.RatingRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderService {
    private final OrderRepository orderRepository;
    private final DeliveryService deliveryService;
    private final RatingRepository ratingRepository;

    public OrderService(OrderRepository orderRepository, DeliveryService deliveryService, RatingRepository ratingRepository) {
        this.orderRepository = orderRepository;
        this.deliveryService = deliveryService;
        this.ratingRepository = ratingRepository;
    }

    public Order placeOrder(Customer customer, Restaurant restaurant, List<MenuItem> items) {
        if (!restaurant.isAcceptingOrders()) {
            throw new IllegalStateException("Restaurant is not accepting orders currently.");
        }
        Order order = new Order(UUID.randomUUID().toString(), customer, restaurant, items);
        orderRepository.save(order);
        return order;
    }

    public boolean confirmOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
        if (order.getStatus() == OrderStatus.PLACED) {
            order.setStatus(OrderStatus.CONFIRMED);
            
            // Assign agent dynamically
            DeliveryAgent agent = deliveryService.assignAgent(order.getDeliveryPincode());
            if (agent != null) {
                order.setDeliveryAgent(agent);
                agent.setAvailable(false);
                order.setStatus(OrderStatus.ASSIGNED);
            } else {
                System.out.println("Warning: Order Confirmed but no delivery agents available to assign.");
            }
            return true;
        }
        return false;
    }

    public void deliverOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
        if (order.getStatus() == OrderStatus.ASSIGNED) {
            order.setStatus(OrderStatus.DELIVERED);
            if (order.getDeliveryAgent() != null) {
                order.getDeliveryAgent().setAvailable(true);
                order.getDeliveryAgent().setCurrentPincode(order.getDeliveryPincode());
            }
        } else {
             throw new IllegalStateException("Order must be ASSIGNED to be DELIVERED.");
        }
    }

    public void closeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
        if (order.getStatus() == OrderStatus.DELIVERED) {
            order.setStatus(OrderStatus.CLOSED);
        } else {
            throw new IllegalStateException("Order must be DELIVERED to be CLOSED.");
        }
    }

    public void rateOrder(String orderId, int restaurantRating, int deliveryAgentRating, String feedback) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
        if (order.getStatus() != OrderStatus.DELIVERED && order.getStatus() != OrderStatus.CLOSED) {
             throw new IllegalStateException("Cannot rate an order that is not delivered or closed.");
        }
        Rating rating = new Rating(UUID.randomUUID().toString(), orderId, restaurantRating, deliveryAgentRating, feedback);
        ratingRepository.save(rating);
    }
    
    public List<Order> getOrdersForUser(User user) {
        return orderRepository.findAll().stream().filter(o -> {
            if (user.getRole() == Role.CUSTOMER) return o.getCustomer().getId().equals(user.getId());
            if (user.getRole() == Role.RESTAURANT) return o.getRestaurant().getId().equals(user.getId());
            if (user.getRole() == Role.DELIVERY_AGENT && o.getDeliveryAgent() != null) return o.getDeliveryAgent().getId().equals(user.getId());
            return false;
        }).collect(Collectors.toList());
    }
}
