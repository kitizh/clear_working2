package com.restaurant.controllers;

import com.restaurant.entities.Recipe;
import com.restaurant.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    public String showRecipes(Model model) {
        List<Recipe> recipes = recipeService.getAllRecipes();

        // Группируем уникальные названия блюд по типу меню
        Map<String, Set<String>> recipesByType = recipes.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getMenu().getMenuType(),
                        Collectors.mapping(r -> r.getMenu().getDishName(), Collectors.toSet())
                ));

        // Группируем ингредиенты по названию блюда
        Map<String, List<Recipe>> ingredientsByDish = recipes.stream()
                .collect(Collectors.groupingBy(r -> r.getMenu().getDishName()));

        model.addAttribute("recipesByType", recipesByType);
        model.addAttribute("ingredientsByDish", ingredientsByDish);
        model.addAttribute("menuTypes", recipesByType.keySet());
        model.addAttribute("isAdmin", true); // TODO: Подключить проверку роли админа

        return "recipes";
    }
}
