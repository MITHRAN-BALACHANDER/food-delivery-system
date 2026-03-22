package com.fooddelivery.repository;

import com.fooddelivery.model.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepository {
    private final Map<String, User> userById = new HashMap<>();
    private final Map<String, User> userByEmail = new HashMap<>();

    public void save(User user) {
        userById.put(user.getId(), user);
        userByEmail.put(user.getEmail(), user);
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(userById.get(id));
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userByEmail.get(email));
    }

    public Collection<User> findAll() {
        return userById.values();
    }
}
