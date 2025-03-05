package com.restaurant.repositories;

import com.restaurant.entities.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderOrderId(Long orderId);
    List<OrderItem> findAllByOrder_OrderTimeBetween(LocalDateTime start, LocalDateTime end);
    @Query("SELECT oi.menu, COUNT(oi) FROM OrderItem oi WHERE oi.order.orderDate BETWEEN :startDate AND :endDate GROUP BY oi.menu ORDER BY COUNT(oi) DESC")
    List<Object[]> findTopDishesByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

}
