package com.restaurant.repositories;

import com.restaurant.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByOrderTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT AVG(o.totalAmount) FROM Orders o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    Double calculateAverageCheckByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
