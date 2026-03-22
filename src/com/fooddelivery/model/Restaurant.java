package com.fooddelivery.model;

import java.util.ArrayList;
import java.util.List;

public class Restaurant extends User {
    private String address;
    private String pincode;
    private List<MenuItem> menu;
    private boolean isAcceptingOrders;

    public Restaurant(String id, String name, String email, String password, String address, String pincode) {
        super(id, name, email, password, Role.RESTAURANT);
        this.address = address;
        this.pincode = pincode;
        this.menu = new ArrayList<>();
        this.isAcceptingOrders = true;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public List<MenuItem> getMenu() { return menu; }
    public void addMenuItem(MenuItem item) { this.menu.add(item); }
    public void removeMenuItem(MenuItem item) { this.menu.remove(item); }
    public boolean isAcceptingOrders() { return isAcceptingOrders; }
    public void setAcceptingOrders(boolean acceptingOrders) { isAcceptingOrders = acceptingOrders; }
}
