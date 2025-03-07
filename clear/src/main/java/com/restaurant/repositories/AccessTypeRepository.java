package com.restaurant.repositories;

import com.restaurant.entities.AccessType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью {@link AccessType}.
 * Содержит методы для работы с типами доступа пользователей.
 */
public interface AccessTypeRepository extends JpaRepository<AccessType, Long> {

}
