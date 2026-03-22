package com.fooddelivery.model;

public class Customer extends User {
    private String address;
    private String pincode;

    public Customer(String id, String name, String email, String password, String address, String pincode) {
        super(id, name, email, password, Role.CUSTOMER);
        this.address = address;
        this.pincode = pincode;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
}
