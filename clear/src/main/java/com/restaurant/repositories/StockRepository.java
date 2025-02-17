package com.restaurant.repositories;

import com.restaurant.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByItem_ItemName(String itemName);
}

