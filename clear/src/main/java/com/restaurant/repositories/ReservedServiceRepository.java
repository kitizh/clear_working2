package com.restaurant.repositories;

import com.restaurant.entities.ReservedService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservedServiceRepository extends JpaRepository<ReservedService, Long> {
    List<ReservedService> findByReservation_Id(Long reservationId);
}
