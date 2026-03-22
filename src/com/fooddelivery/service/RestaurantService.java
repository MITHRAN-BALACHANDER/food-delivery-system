package com.fooddelivery.service;

import com.fooddelivery.model.MenuItem;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.model.Role;
import com.fooddelivery.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantService {
    private final UserRepository userRepository;

    public RestaurantService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Restaurant> getAllRestaurants() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.RESTAURANT)
                .map(u -> (Restaurant) u)
                .collect(Collectors.toList());
    }

    public void addMenuItem(Restaurant restaurant, MenuItem item) {
        restaurant.addMenuItem(item);
    }

    public void removeMenuItem(Restaurant restaurant, MenuItem item) {
        restaurant.removeMenuItem(item);
    }
    
    public void setAcceptingOrders(Restaurant restaurant, boolean status) {
        restaurant.setAcceptingOrders(status);
    }
}
