package com.fooddelivery.model;

import java.util.List;

public class Order {
    private String id;
    private Customer customer;
    private Restaurant restaurant;
    private DeliveryAgent deliveryAgent;
    private List<MenuItem> items;
    private OrderStatus status;
    private double totalAmount;
    private String deliveryPincode;

    public Order(String id, Customer customer, Restaurant restaurant, List<MenuItem> items) {
        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = items;
        this.status = OrderStatus.PLACED;
        this.deliveryPincode = customer.getPincode();
        this.totalAmount = items.stream().mapToDouble(MenuItem::getPrice).sum();
    }

    public String getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Restaurant getRestaurant() { return restaurant; }
    public DeliveryAgent getDeliveryAgent() { return deliveryAgent; }
    public void setDeliveryAgent(DeliveryAgent deliveryAgent) { this.deliveryAgent = deliveryAgent; }
    public List<MenuItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public String getDeliveryPincode() { return deliveryPincode; }
}
