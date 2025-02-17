package com.restaurant.repositories;

import com.restaurant.entities.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    Unit findByUnitName(String unitName);
}

