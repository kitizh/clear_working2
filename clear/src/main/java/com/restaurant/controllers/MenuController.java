package com.restaurant.controllers;

import com.restaurant.entities.Menu;
import com.restaurant.entities.MenuType;
import com.restaurant.repositories.MenuRepository;
import com.restaurant.repositories.MenuTypeRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/menu")
public class MenuController {

    private final MenuRepository menuRepository;
    private final MenuTypeRepository menuTypeRepository;

    public MenuController(MenuRepository menuRepository, MenuTypeRepository menuTypeRepository) {
        this.menuRepository = menuRepository;
        this.menuTypeRepository = menuTypeRepository;
    }

    @GetMapping
    public String getMenu(Model model, Authentication authentication) {
        List<MenuType> menuTypes = menuTypeRepository.findAll();

        if (menuTypes.isEmpty()) {
            model.addAttribute("error", "No menu types found.");
            return "menu";
        }

        Map<String, List<Menu>> menuItems = menuTypes.stream()
                .collect(Collectors.toMap(MenuType::getTypeName, type ->
                        menuRepository.findByMenuType(type) != null ? menuRepository.findByMenuType(type) : new ArrayList<>()));

        model.addAttribute("menuTypes", menuTypes);
        model.addAttribute("menuItems", menuItems);

        // Проверяем, является ли пользователь админом
        boolean isAdmin = authentication != null && authentication.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);

        return "menu";
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String addMenuForm(Model model) {
        model.addAttribute("menuTypes", menuTypeRepository.findAll());
        return "menu-add";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addMenu(@RequestParam String dishName,
                          @RequestParam String description,
                          @RequestParam Double price,
                          @RequestParam Long menuTypeId) {
        MenuType menuType = menuTypeRepository.findById(menuTypeId).orElseThrow();
        Menu menu = new Menu();
        menu.setDishName(dishName);
        menu.setDescription(description);
        menu.setPrice(price);
        menu.setMenuType(menuType);
        menuRepository.save(menu);
        return "redirect:/menu";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editMenuForm(@PathVariable Long id, Model model) {
        Menu menu = menuRepository.findById(id).orElseThrow();
        model.addAttribute("menu", menu);
        model.addAttribute("menuTypes", menuTypeRepository.findAll());
        return "menu-edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String editMenu(@PathVariable Long id,
                           @RequestParam String dishName,
                           @RequestParam String description,
                           @RequestParam Double price,
                           @RequestParam Long menuTypeId) {
        Menu menu = menuRepository.findById(id).orElseThrow();
        MenuType menuType = menuTypeRepository.findById(menuTypeId).orElseThrow();
        menu.setDishName(dishName);
        menu.setDescription(description);
        menu.setPrice(price);
        menu.setMenuType(menuType);
        menuRepository.save(menu);
        return "redirect:/menu";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteMenu(@PathVariable Long id) {
        menuRepository.deleteById(id);
        return "redirect:/menu";
    }
}
