package com.restaurant.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.entities.AllItems;
import com.restaurant.entities.Menu;
import com.restaurant.entities.Recipe;
import com.restaurant.repositories.AllItemsRepository;
import com.restaurant.repositories.MenuRepository;
import com.restaurant.repositories.RecipeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final MenuRepository menuRepository;
    private final AllItemsRepository allItemsRepository;
    private final ObjectMapper objectMapper;

    public RecipeController(RecipeRepository recipeRepository,
                            MenuRepository menuRepository,
                            AllItemsRepository allItemsRepository,
                            ObjectMapper objectMapper) {
        this.recipeRepository = recipeRepository;
        this.menuRepository = menuRepository;
        this.allItemsRepository = allItemsRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String showRecipes(Model model, Authentication authentication) throws Exception {
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

        // Мапа для передачи идентификатора блюда (Menu) по названию
        Map<String, Long> dishMenuMap = recipes.stream()
                .collect(Collectors.toMap(r -> r.getMenu().getDishName(), r -> r.getMenu().getMenuId(), (a, b) -> a));

        model.addAttribute("recipesByType", recipesByType);
        model.addAttribute("ingredientsByDish", ingredientsByDish);
        model.addAttribute("menuTypes", recipesByType.keySet());
        model.addAttribute("dishMenuMap", dishMenuMap);

        boolean isAdmin = authentication != null && authentication.getAuthorities()
                .stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        // Для выпадающего списка типов продуктов используем уникальные типы из all_items
        List<String> productTypes = allItemsRepository.findAll().stream()
                .map(AllItems::getProductType)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("productTypes", productTypes);

        Map<String, List<AllItems>> allItemsMap = allItemsRepository.findAll().stream()
                .collect(Collectors.groupingBy(AllItems::getProductType));
        String allItemsJson = objectMapper.writeValueAsString(allItemsMap);
        model.addAttribute("allItemsJson", allItemsJson);

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
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        recipeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Метод добавления нового ингредиента (рецепта) для блюда.
    // Ожидает JSON с полями: menuId, productType, itemName, amount, unit.
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/ingredient/add")
    @ResponseBody
    public ResponseEntity<Recipe> addIngredient(@RequestBody Map<String, String> payload) {
        String menuIdStr = payload.get("menuId");
        String productType = payload.get("productType");
        String itemName = payload.get("itemName");
        String amountStr = payload.get("amount");
        String unit = payload.get("unit");

        if (menuIdStr == null || menuIdStr.trim().isEmpty() ||
                productType == null || productType.trim().isEmpty() ||
                itemName == null || itemName.trim().isEmpty() ||
                amountStr == null || amountStr.trim().isEmpty() ||
                unit == null || unit.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Long menuId = Long.parseLong(menuIdStr);
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NoSuchElementException("Menu not found"));
        // Ищем продукт в all_items; если отсутствует – создаём его
        AllItems item = allItemsRepository.findByItemName(itemName);
        if(item == null) {
            item = new AllItems();
            item.setItemName(itemName);
            item.setProductType(productType);
            item = allItemsRepository.save(item);
        }
        Recipe recipe = new Recipe();
        recipe.setMenu(menu);
        recipe.setItem(item);
        recipe.setAmount(amount);
        recipe.setUnit(unit);
        Recipe saved = recipeRepository.save(recipe);
        return ResponseEntity.ok(saved);
    }
}