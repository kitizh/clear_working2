package com.restaurant.controllers;

import com.restaurant.entities.Menu;
import com.restaurant.repositories.RecipeRepository;
import com.restaurant.repositories.MenuRepository;
import com.restaurant.entities.Recipe;
import com.restaurant.services.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeService recipeService, RecipeRepository recipeRepository) {
        this.recipeService = recipeService;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping
    public String showRecipes(Model model, Authentication authentication) {
        List<Recipe> recipes = recipeRepository.findAll();

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
        boolean isAdmin = authentication != null && authentication.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin); // TODO: Подключить проверку роли админа

        return "recipes";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateRecipe(@RequestBody List<Recipe> updatedRecipes) {
        for (Recipe updatedRecipe : updatedRecipes) {
            Recipe recipe = recipeRepository.findById(updatedRecipe.getRecipeId())
                    .orElseThrow(() -> new NoSuchElementException("Recipe not found"));
            recipe.setAmount(updatedRecipe.getAmount());
            recipe.setUnit(updatedRecipe.getUnit());
            recipeRepository.save(recipe);
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteMenu(@PathVariable Long id) {
        recipeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
