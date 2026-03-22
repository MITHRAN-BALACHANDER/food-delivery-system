package com.fooddelivery.service;

import com.fooddelivery.model.DeliveryAgent;
import com.fooddelivery.model.Role;
import com.fooddelivery.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DeliveryService {
    private final UserRepository userRepository;

    public DeliveryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Smart logic: find nearest or first available
    public DeliveryAgent assignAgent(String targetPincode) {
        List<DeliveryAgent> agents = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.DELIVERY_AGENT)
                .map(u -> (DeliveryAgent) u)
                .filter(DeliveryAgent::isAvailable)
                .collect(Collectors.toList());

        if (agents.isEmpty()) {
            return null; // No agents available
        }

        // Try to assign one with matching pincode (nearest)
        for (DeliveryAgent agent : agents) {
            if (agent.getCurrentPincode().equals(targetPincode)) {
                return agent;
            }
        }

        // Fallback: first available
        return agents.get(0);
    }
}
