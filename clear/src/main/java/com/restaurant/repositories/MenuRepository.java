package com.restaurant.repositories;

import com.restaurant.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByMenuType(String menuType); // Было MenuType, теперь String
}
