package com.fooddelivery.model;

public class DeliveryAgent extends User {
    private boolean isAvailable;
    private String currentPincode;

    public DeliveryAgent(String id, String name, String email, String password, String currentPincode) {
        super(id, name, email, password, Role.DELIVERY_AGENT);
        this.isAvailable = true;
        this.currentPincode = currentPincode;
    }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public String getCurrentPincode() { return currentPincode; }
    public void setCurrentPincode(String currentPincode) { this.currentPincode = currentPincode; }
}
