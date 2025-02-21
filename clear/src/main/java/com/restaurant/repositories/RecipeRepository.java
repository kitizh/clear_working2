package com.restaurant.repositories;

import com.restaurant.entities.Menu;
import com.restaurant.entities.Recipe;
import com.restaurant.entities.MenuType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // Поиск рецептов по типу блюда через связь с Menu
    List<Recipe> findByMenu_MenuType(MenuType menuType);

    List<Recipe> findByMenu(Menu dish);
}
