package com.restaurant.repositories;

import com.restaurant.entities.AllTables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью {@link AllTables}.
 * Содержит методы для работы с таблицами ресторана.
 */
@Repository
public interface AllTablesRepository extends JpaRepository<AllTables, Integer> {
}
