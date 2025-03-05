package com.restaurant.repositories;

import com.restaurant.entities.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r.reservationTime, COUNT(r) FROM Reservation r WHERE r.reservationDate BETWEEN :startDate AND :endDate GROUP BY r.reservationTime ORDER BY COUNT(r) DESC")
    List<Object[]> findTopReservationTimesByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    @Query("SELECT r.table, COUNT(r) FROM Reservation r WHERE r.reservationDate BETWEEN :startDate AND :endDate GROUP BY r.table ORDER BY COUNT(r) DESC")
    List<Object[]> findTopTablesByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

}
