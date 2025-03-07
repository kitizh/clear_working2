package com.restaurant.repositories;

import com.restaurant.entities.ReservedService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link ReservedService}.
 * Содержит методы для работы с зарезервированными услугами в ресторане.
 */
@Repository
public interface ReservedServiceRepository extends JpaRepository<ReservedService, Long> {

    /**
     * Находит все зарезервированные услуги для указанного бронирования.
     *
     * @param reservationId Идентификатор бронирования.
     * @return Список зарезервированных услуг для указанного бронирования.
     */
    List<ReservedService> findByReservation_Id(Long reservationId);
}
