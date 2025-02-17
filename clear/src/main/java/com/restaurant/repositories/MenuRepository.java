package com.restaurant.repositories;

import com.restaurant.entities.Menu;
import com.restaurant.entities.MenuType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByMenuType(MenuType menuType);
}
