package com.restaurant.repositories;

import com.restaurant.entities.AllTables;
import com.restaurant.entities.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Reservation}.
 * Содержит методы для работы с бронированиями в ресторане.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Находит наиболее популярные времена для бронирований в указанный период.
     *
     * @param startDate Дата начала интервала.
     * @param endDate Дата окончания интервала.
     * @param pageable Параметры пагинации.
     * @return Список времён с количеством бронирований в каждое время.
     */
    @Query("SELECT r.reservationTime, COUNT(r) FROM Reservation r WHERE r.reservationDate BETWEEN :startDate AND :endDate GROUP BY r.reservationTime ORDER BY COUNT(r) DESC")
    List<Object[]> findTopReservationTimesByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

    /**
     * Находит наиболее популярные столы для бронирований в указанный период.
     *
     * @param startDate Дата начала интервала.
     * @param endDate Дата окончания интервала.
     * @param pageable Параметры пагинации.
     * @return Список столов с количеством бронирований для каждого стола.
     */
    @Query("SELECT r.table, COUNT(r) FROM Reservation r WHERE r.reservationDate BETWEEN :startDate AND :endDate GROUP BY r.table ORDER BY COUNT(r) DESC")
    List<Object[]> findTopTablesByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

    /**
     * Находит все бронирования для указанного стола в определённую дату.
     *
     * @param table Стол, для которого ищутся бронирования.
     * @param reservationDate Дата бронирования.
     * @return Список бронирований для указанного стола в заданную дату.
     */
    List<Reservation> findByTableAndReservationDate(AllTables table, LocalDate reservationDate);
}
