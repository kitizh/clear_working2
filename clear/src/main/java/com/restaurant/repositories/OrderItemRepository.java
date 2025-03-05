package com.restaurant.repositories;

import com.restaurant.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderOrderId(Long orderId);
    List<OrderItem> findAllByOrder_OrderTimeBetween(LocalDateTime start, LocalDateTime end);
}
