package com.restaurant.repositories;

import com.restaurant.entities.MenuType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuTypeRepository extends JpaRepository<MenuType, Long> {
    MenuType findByTypeName(String typeName);
}
