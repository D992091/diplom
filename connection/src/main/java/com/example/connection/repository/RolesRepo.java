package com.example.connection.repository;

import com.example.connection.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RolesRepo extends JpaRepository<Roles,Integer> {
    @Query(value = "Select max(id) from user_roles", nativeQuery = true)
    Integer findMinimum();
}
