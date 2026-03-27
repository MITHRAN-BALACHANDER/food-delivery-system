package com.fooddelivery.model;

public class Rating {
    private String id;
    private String orderId;
    private int restaurantRating;
    private int deliveryAgentRating;
    private String feedback;

    public Rating(String id, String orderId, int restaurantRating, int deliveryAgentRating, String feedback) {
        this.id = id;
        this.orderId = orderId;
        this.restaurantRating = restaurantRating;
        this.deliveryAgentRating = deliveryAgentRating;
        this.feedback = feedback;
    }

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public int getRestaurantRating() { return restaurantRating; }
    public int getDeliveryAgentRating() { return deliveryAgentRating; }
    public String getFeedback() { return feedback; }
}
