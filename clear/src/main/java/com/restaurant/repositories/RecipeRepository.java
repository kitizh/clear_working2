package com.restaurant.repositories;

import com.restaurant.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAll();

    List<Recipe> findByMenu_MenuId(Long menuId);

    List<Recipe> findByMenuMenuId(Long menuId);
}
