package com.restaurant.repositories;

import com.restaurant.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Orders}.
 * Содержит методы для работы с заказами в ресторане.
 */
@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    /**
     * Вычисляет средний чек за указанный период времени.
     *
     * @param startDate Дата начала интервала.
     * @param endDate Дата окончания интервала.
     * @return Средний чек за указанный период.
     */
    @Query("SELECT AVG(o.totalAmount) FROM Orders o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    Double calculateAverageCheckByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
