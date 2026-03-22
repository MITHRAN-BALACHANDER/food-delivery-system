package com.fooddelivery.model;

public class MenuItem {
    private String id;
    private String name;
    private double price;
    private String idRestaurant;

    public MenuItem(String id, String name, double price, String idRestaurant) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.idRestaurant = idRestaurant;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getIdRestaurant() { return idRestaurant; }
}
