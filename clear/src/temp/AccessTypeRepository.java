package com.restaurant.repositories;

import com.restaurant.entities.AccessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTypeRepository extends JpaRepository<AccessType, Integer> {

    // Custom method to find AccessType by its ID

    Optional<AccessType> findByAccessId(AccessType accessId);
}