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

/**
 * Репозиторий для работы с сущностью {@link OrderItem}.
 * Содержит методы для работы с заказами и блюдами в заказах.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Находит все позиции в заказе по идентификатору заказа.
     *
     * @param orderId Идентификатор заказа.
     * @return Список позиций заказа.
     */
    List<OrderItem> findByOrderOrderId(Long orderId);

    /**
     * Находит самые популярные блюда за указанный период времени.
     * Результат отсортирован по количеству заказов.
     *
     * @param startDate Дата начала интервала.
     * @param endDate Дата окончания интервала.
     * @param pageable Параметры пагинации.
     * @return Список блюд с их количеством.
     */
    @Query("SELECT oi.menu, COUNT(oi) FROM OrderItem oi WHERE oi.order.orderDate BETWEEN :startDate AND :endDate GROUP BY oi.menu ORDER BY COUNT(oi) DESC")
    List<Object[]> findTopDishesByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
}
