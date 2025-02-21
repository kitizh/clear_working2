package com.restaurant.controllers;

import com.restaurant.entities.*;
import com.restaurant.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final MenuRepository menuRepository;
    private final StockRepository stockRepository;
    private final UnitRepository unitRepository;
    private final MenuTypeRepository menuTypeRepository;

    public RecipeController(RecipeRepository recipeRepository, MenuRepository menuRepository,
                            StockRepository stockRepository, UnitRepository unitRepository,
                            MenuTypeRepository menuTypeRepository) {
        this.recipeRepository = recipeRepository;
        this.menuRepository = menuRepository;
        this.stockRepository = stockRepository;
        this.unitRepository = unitRepository;
        this.menuTypeRepository = menuTypeRepository;
    }

    @GetMapping
    public String getRecipes(Model model) {
        List<MenuType> menuTypes = menuTypeRepository.findAll();
        Map<String, List<Menu>> menuItems = new HashMap<>();
        Map<String, List<Recipe>> recipesByDish = new HashMap<>();

        for (MenuType type : menuTypes) {
            List<Menu> dishes = menuRepository.findByMenuType(type);
            menuItems.put(type.getTypeName(), dishes);

            for (Menu dish : dishes) {
                List<Recipe> recipes = recipeRepository.findByMenu(dish);
                recipesByDish.put(dish.getDishName(), recipes);
            }
        }

        model.addAttribute("menuTypes", menuTypes);
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("recipes", recipesByDish);
        return "recipe";
    }


    @PostMapping("/add")
    public String addRecipe(@RequestParam Long menuId,
                            @RequestParam Long stockId,
                            @RequestParam Double amount,
                            @RequestParam Long unitId) {
        Recipe recipe = new Recipe();
        recipe.setMenu(menuRepository.findById(menuId).orElseThrow(() -> new NoSuchElementException("Menu not found")));
        recipe.setStock(stockRepository.findById(stockId).orElseThrow(() -> new NoSuchElementException("Stock item not found")));
        recipe.setAmount(amount);
        recipe.setUnit(unitRepository.findById(unitId).orElseThrow(() -> new NoSuchElementException("Unit not found")));

        recipeRepository.save(recipe);
        return "redirect:/recipe";
    }

    @PostMapping("/update")
    public String updateRecipe(@RequestParam Long recipeId,
                               @RequestParam Double amount,
                               @RequestParam Long unitId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NoSuchElementException("Recipe not found"));

        recipe.setAmount(amount);
        recipe.setUnit(unitRepository.findById(unitId)
                .orElseThrow(() -> new NoSuchElementException("Unit not found")));

        recipeRepository.save(recipe);
        return "redirect:/recipe";
    }

    @PostMapping("/delete")
    public String deleteRecipe(@RequestParam Long recipeId) {
        recipeRepository.deleteById(recipeId);
        return "redirect:/recipe";
    }
}
