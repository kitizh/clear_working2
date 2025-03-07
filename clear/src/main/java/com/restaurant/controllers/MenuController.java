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

/**
 * Контроллер для работы с меню ресторана.
 * Позволяет получать меню, обновлять, удалять и добавлять блюда.
 */
@Controller
@RequestMapping("/menu")
public class MenuController {

    private final MenuRepository menuRepository;

    /**
     * Конструктор для инициализации репозитория меню.
     *
     * @param menuRepository Репозиторий для работы с меню.
     */
    public MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    /**
     * Обрабатывает запрос на получение страницы меню.
     * Отображает список всех блюд, сгруппированных по типу.
     * Также проверяет роль пользователя и передает её в модель.
     *
     * @param model Модель, в которую добавляются данные для отображения.
     * @param authentication Информация о текущем аутентифицированном пользователе.
     * @return Имя шаблона для страницы меню.
     */
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

        // Проверяем роль пользователя и передаем её в модель
        String role = authentication != null ? authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("GUEST") : "GUEST";  // Если не аутентифицирован, то роль GUEST

        model.addAttribute("role", role);

        boolean isAdmin = role.equals("ROLE_COOK") || role.equals("ROLE_ADMIN");
        model.addAttribute("isAdmin", isAdmin);

        return "menu";  // Возвращаем имя шаблона для страницы меню
    }

    /**
     * Обрабатывает запрос на обновление блюда в меню.
     * Доступно только пользователям с ролью ADMIN.
     *
     * @param updatedMenus Список обновленных блюд.
     * @return Ответ с кодом 200 OK при успешном обновлении.
     */
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
            menuRepository.save(menu);  // Сохраняем обновленное блюдо
        }
        return ResponseEntity.ok().build();  // Возвращаем успешный ответ
    }

    /**
     * Обрабатывает запрос на удаление блюда из меню по его ID.
     * Доступно только пользователям с ролью ADMIN.
     *
     * @param id ID блюда, которое нужно удалить.
     * @return Ответ с кодом 200 OK при успешном удалении.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteMenu(@PathVariable Long id) {
        menuRepository.deleteById(id);  // Удаляем блюдо по ID
        return ResponseEntity.ok().build();  // Возвращаем успешный ответ
    }

    /**
     * Обрабатывает запрос на добавление нового блюда в меню.
     * Доступно только пользователям с ролью ADMIN.
     *
     * @param newMenu Новый объект блюда, который нужно добавить в меню.
     * @return Ответ с новым добавленным блюдом.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Menu> addMenuItem(@RequestBody Menu newMenu) {
        // Если описание не указано, задаем дефолтное значение
        if (newMenu.getDescription() == null || newMenu.getDescription().isEmpty()) {
            newMenu.setDescription("-");  // Устанавливаем дефолтное описание
        }
        // Устанавливаем цену по умолчанию равной 0
        newMenu.setPrice(0.0);
        // Сохраняем новое блюдо в БД
        Menu savedMenu = menuRepository.save(newMenu);
        return ResponseEntity.ok(savedMenu);  // Возвращаем добавленное блюдо
    }

}
