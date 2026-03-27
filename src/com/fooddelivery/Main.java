package com.fooddelivery;

import com.fooddelivery.model.*;
import com.fooddelivery.repository.*;
import com.fooddelivery.service.*;
import com.fooddelivery.ui.ConsoleUI;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
    
        UserRepository userRepository = new UserRepository();
        OrderRepository orderRepository = new OrderRepository();
        RatingRepository ratingRepository = new RatingRepository();

        AuthService authService = new AuthService(userRepository);
        RestaurantService restaurantService = new RestaurantService(userRepository);
        DeliveryService deliveryService = new DeliveryService(userRepository);
        OrderService orderService = new OrderService(orderRepository, deliveryService, ratingRepository);
        AnalyticsService analyticsService = new AnalyticsService(orderRepository, ratingRepository, userRepository);

        seedData(userRepository, restaurantService);

        ConsoleUI consoleUI = new ConsoleUI(authService, restaurantService, orderService, deliveryService, analyticsService);
        consoleUI.start();
    }

    private static void seedData(UserRepository userRepository, RestaurantService restaurantService) {
        Customer c1 = new Customer(UUID.randomUUID().toString(), "Mithran", "mithran@gmail.com", "mithran123", "123 Main St", "10001");
        userRepository.save(c1);

        Restaurant r1 = new Restaurant(UUID.randomUUID().toString(), "Burger ", "burger@gmail.com", "pass", "burger St", "10001");
        userRepository.save(r1);
        restaurantService.addMenuItem(r1, new MenuItem(UUID.randomUUID().toString(), "Whopper", 5.99, r1.getId()));
        restaurantService.addMenuItem(r1, new MenuItem(UUID.randomUUID().toString(), "Fries", 2.99, r1.getId()));

        DeliveryAgent d1 = new DeliveryAgent(UUID.randomUUID().toString(), "delivery Agent", "delivery@gmail.com", "delivery123", "10001");
        userRepository.save(d1);
    }
}
