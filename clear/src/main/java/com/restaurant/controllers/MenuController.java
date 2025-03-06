package com.restaurant.controllers;

import com.restaurant.entities.Menu;
import com.restaurant.repositories.MenuRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

        model.addAttribute("menuTypes", menuTypes);
        model.addAttribute("menuItems", menuItems);

        // Проверяем, является ли пользователь администратором

        String role = authentication != null ? authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("GUEST") : "GUEST";  // Если не аутентифицирован, то роль GUEST

        // Передаем роль в модель
        model.addAttribute("role", role);

        boolean isAdmin = role.equals("ROLE_COOK") || role.equals("ROLE_ADMIN");;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Menu> addMenuItem(@RequestBody Menu newMenu) {
        // Если описание не указано, задаем дефолтное значение
        if (newMenu.getDescription() == null || newMenu.getDescription().isEmpty()) {
            newMenu.setDescription("-");
        }
        // Устанавливаем цену по умолчанию равной 0
        newMenu.setPrice(0.0);
        // Сохраняем новое блюдо в БД
        Menu savedMenu = menuRepository.save(newMenu);
        return ResponseEntity.ok(savedMenu);
    }

}
