package com.restaurant.controllers;

import com.restaurant.entities.Menu;
import com.restaurant.entities.MenuType;
import com.restaurant.entities.Recipe;
import com.restaurant.repositories.MenuRepository;
import com.restaurant.repositories.MenuTypeRepository;
import com.restaurant.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recipe")
public class RecipeController {

    private final MenuRepository menuRepository;
    private final MenuTypeRepository menuTypeRepository;
    private final RecipeRepository recipeRepository;

    public RecipeController(MenuRepository menuRepository, MenuTypeRepository menuTypeRepository, RecipeRepository recipeRepository) {
        this.menuRepository = menuRepository;
        this.menuTypeRepository = menuTypeRepository;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping
    public String getRecipes(Model model) {
        List<MenuType> menuTypes = menuTypeRepository.findAll();
        List<Menu> allMenus = menuRepository.findAll();

        if (allMenus.isEmpty()) {
            model.addAttribute("error", "No menu items found.");
            return "recipe";
        }

        Map<String, List<Menu>> menuItems = menuTypes.stream()
                .collect(Collectors.toMap(MenuType::getTypeName, menuRepository::findByMenuType));

        Map<Long, List<Recipe>> recipes = allMenus.stream()
                .collect(Collectors.toMap(
                        Menu::getMenuId,
                        m -> {
                            List<Recipe> menuRecipes = recipeRepository.findByMenu(m);
                            return menuRecipes != null ? menuRecipes : List.of(); // Теперь null заменяем на пустой список
                        }
                ));

        model.addAttribute("menuTypes", menuTypes);
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("recipes", recipes);
        return "recipe";
    }
}
