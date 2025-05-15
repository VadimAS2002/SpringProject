package com.example.demo.repository;

import com.example.demo.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findUserByUsername(String username);
    User createUser(User user);
    Optional<User> findById(Long id);
}
