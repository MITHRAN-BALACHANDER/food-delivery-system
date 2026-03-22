package com.fooddelivery.repository;

import com.fooddelivery.model.Rating;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RatingRepository {
    private final Map<String, Rating> ratingById = new HashMap<>();
    private final Map<String, Rating> ratingByOrderId = new HashMap<>();

    public void save(Rating rating) {
        ratingById.put(rating.getId(), rating);
        ratingByOrderId.put(rating.getOrderId(), rating);
    }

    public Optional<Rating> findById(String id) {
        return Optional.ofNullable(ratingById.get(id));
    }

    public Optional<Rating> findByOrderId(String orderId) {
        return Optional.ofNullable(ratingByOrderId.get(orderId));
    }

    public Collection<Rating> findAll() {
        return ratingById.values();
    }
}
