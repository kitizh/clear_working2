package com.restaurant.controllers;

import com.restaurant.entities.Menu;
import com.restaurant.repositories.MenuRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/menu")
public class MenuController {

    private final MenuRepository menuRepository;

    public MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @GetMapping
    public String getMenu(Model model, Authentication authentication) {
        // Получаем все типы блюд (группируем по menuType)
        List<String> menuTypes = menuRepository.findAll().stream()
                .map(Menu::getMenuType)
                .distinct()
                .collect(Collectors.toList());

        // Создаём карту <Тип блюда, Список блюд>
        Map<String, List<Menu>> menuItems = new HashMap<>();
        for (String type : menuTypes) {
            List<Menu> dishes = menuRepository.findByMenuType(type);
            menuItems.put(type, dishes);
        }

        // 🔍 Логирование
        System.out.println("----- ТИПЫ БЛЮД -----");
        menuTypes.forEach(System.out::println);
        System.out.println("----- МЕНЮ -----");
        menuItems.forEach((key, value) -> System.out.println(key + " -> " + value.size() + " блюд"));

        model.addAttribute("menuTypes", menuTypes);
        model.addAttribute("menuItems", menuItems);

        // Проверяем, является ли пользователь администратором
        boolean isAdmin = authentication != null && authentication.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);

        return "menu";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateMenu(@RequestBody List<Menu> updatedMenus) {
        for (Menu updatedMenu : updatedMenus) {
            Menu menu = menuRepository.findById(updatedMenu.getMenuId())
                    .orElseThrow(() -> new NoSuchElementException("Menu not found"));
            menu.setDishName(updatedMenu.getDishName());
            menu.setDescription(updatedMenu.getDescription());
            menu.setPrice(updatedMenu.getPrice());
            menuRepository.save(menu);
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteMenu(@PathVariable Long id) {
        menuRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
