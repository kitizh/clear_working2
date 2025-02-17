package com.restaurant.repositories;

import com.restaurant.entities.AccessType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTypeRepository extends JpaRepository<AccessType, Long> {
    AccessType findByAccessName(String accessName);
}
