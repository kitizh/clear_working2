package com.restaurant.repositories;

import com.restaurant.entities.Recipe;
import com.restaurant.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByMenu(Menu menu);
}

