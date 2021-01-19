package com.example.connection.repository;

import com.example.connection.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepo extends JpaRepository<Users, Integer> {
    Users findByUsername(String username);
    Iterable<Users> findTop10ByUsername(String username);
    }

