package com.restaurant.repositories;

import com.restaurant.entities.Stock;
import com.restaurant.entities.AllItems;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByItem(AllItems item); // Исправленный метод
}
