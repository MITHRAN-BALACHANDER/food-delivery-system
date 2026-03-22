package com.fooddelivery.service;

import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderStatus;
import com.fooddelivery.model.Rating;
import com.fooddelivery.model.Role;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.repository.RatingRepository;
import com.fooddelivery.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsService {
    private final OrderRepository orderRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    public AnalyticsService(OrderRepository orderRepository, RatingRepository ratingRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }

    public void printTopRestaurants() {
        Map<String, List<Integer>> restaurantRatings = new HashMap<>();
        
        for (Rating r : ratingRepository.findAll()) {
            Order o = orderRepository.findById(r.getOrderId()).orElse(null);
            if (o != null) {
                restaurantRatings.putIfAbsent(o.getRestaurant().getName(), new ArrayList<>());
                restaurantRatings.get(o.getRestaurant().getName()).add(r.getRestaurantRating());
            }
        }

        System.out.println("--- Top Restaurants ---");
        restaurantRatings.entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().stream().mapToInt(Integer::intValue).average().orElse(0)))
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(5)
                .forEach(e -> System.out.printf("%s: %.2f/5.0\n", e.getKey(), e.getValue()));
    }

    public void printDemandByRegion() {
        Map<String, Long> demand = orderRepository.findAll().stream()
                .collect(Collectors.groupingBy(Order::getDeliveryPincode, Collectors.counting()));
                
        System.out.println("--- High Demand Areas (By Pincode) ---");
        demand.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .forEach(e -> System.out.println("Pincode " + e.getKey() + ": " + e.getValue() + " orders"));
    }
}
