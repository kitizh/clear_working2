package com.restaurant.repositories;

import com.restaurant.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью {@link Service}.
 * Содержит методы для работы с услугами ресторана.
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
}
