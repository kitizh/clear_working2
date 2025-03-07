package com.restaurant.repositories;

import com.restaurant.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Recipe}.
 * Содержит методы для работы с рецептами блюд в меню.
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    /**
     * Находит все рецепты.
     *
     * @return Список всех рецептов.
     */
    List<Recipe> findAll();

    /**
     * Находит все рецепты для определённого блюда в меню.
     *
     * @param menuId Идентификатор меню.
     * @return Список рецептов для указанного блюда.
     */
    List<Recipe> findByMenuMenuId(Long menuId);
}
