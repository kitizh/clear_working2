package com.restaurant.repositories;

import com.restaurant.entities.Stock;
import com.restaurant.entities.AllItems;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Stock}.
 * Содержит методы для работы с запасами продуктов.
 */
public interface StockRepository extends JpaRepository<Stock, Long> {

    /**
     * Находит запас по идентификатору товара.
     *
     * @param itemId Идентификатор товара.
     * @return Объект {@link Stock}, представляющий информацию о запасах для указанного товара.
     */
    Stock findByItemItemId(Long itemId);
}
