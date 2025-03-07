package com.restaurant.repositories;

import com.restaurant.entities.AllItems;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью {@link AllItems}.
 * Содержит методы для работы с товарами, которые используются в меню.
 */
public interface AllItemsRepository extends JpaRepository<AllItems, Long> {

    /**
     * Находит товар по его имени.
     *
     * @param itemName Название товара.
     * @return Товар с указанным названием.
     */
    AllItems findByItemName(String itemName);
}
