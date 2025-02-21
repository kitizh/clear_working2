package com.restaurant.repositories;

import com.restaurant.entities.AllItems;
import com.restaurant.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByItem(AllItems allItems);
}

