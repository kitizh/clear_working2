package com.restaurant.repositories;

import com.restaurant.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Menu}.
 * Содержит методы для работы с меню ресторана.
 */
public interface MenuRepository extends JpaRepository<Menu, Long> {

    /**
     * Находит все блюда определённого типа.
     *
     * @param menuType Тип меню (например, "Основные блюда", "Десерты").
     * @return Список блюд указанного типа.
     */
    List<Menu> findByMenuType(String menuType); // Было MenuType, теперь String
}
