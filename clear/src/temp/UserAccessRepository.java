package com.restaurant.repositories;

import com.restaurant.entities.UserAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;

@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, Integer> {

    // Find access levels for a specific user
    Optional<UserAccess> findByUser(User user);


}