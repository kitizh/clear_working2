package com.restaurant.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для обработки запросов на главные страницы веб-приложения.
 * Обрабатывает запросы на главную страницу, страницу "О нас" и страницу логина.
 */
@Controller
public class MainController {

    /**
     * Обрабатывает запрос на главную страницу (index).
     *
     * @return Имя шаблона для главной страницы.
     */
    @GetMapping("/")
    public String getMenuPage() {
        return "index";  // Возвращает имя шаблона для главной страницы
    }

    /**
     * Обрабатывает запрос на страницу "О нас".
     * Получает информацию о роли текущего пользователя и передает её на страницу.
     *
     * @param model Модель, используемая для передачи данных в шаблон.
     * @param authentication Информация о текущем аутентифицированном пользователе.
     * @return Имя шаблона для страницы "О нас".
     */
    @GetMapping("/about")
    public String getAbout(Model model, Authentication authentication) {

        // Получение роли пользователя (если аутентифицирован), иначе роль "GUEST"
        String role = authentication != null ? authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("GUEST") : "GUEST";  // Если пользователь не аутентифицирован, роль будет "GUEST"

        model.addAttribute("role", role);  // Передаем роль пользователя в модель

        return "about";  // Возвращает имя шаблона для страницы "О нас"
    }

    /**
     * Обрабатывает запрос на страницу логина.
     *
     * @return Имя шаблона для страницы логина.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // Возвращает имя шаблона для страницы логина
    }

}
