package com.restaurant.repositories;

import com.restaurant.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByOrderTimeBetween(LocalDateTime start, LocalDateTime end);
}
