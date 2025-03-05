package com.restaurant.repositories;

import com.restaurant.entities.ReservedTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservedTableRepository extends JpaRepository<ReservedTable, Long> {
    List<ReservedTable> findByReservation_Id(Long reservationId);
}
