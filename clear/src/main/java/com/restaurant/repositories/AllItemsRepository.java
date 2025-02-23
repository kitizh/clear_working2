package com.restaurant.repositories;

import com.restaurant.entities.AllItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllItemsRepository extends JpaRepository<AllItems, Long> {
    AllItems findByItemName(String itemName);
}
