package com.restaurant.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String getMenuPage() {
        return "index";
    }

    @GetMapping("/about")
    public String getAbout(Model model, Authentication authentication) {

        String role = authentication != null ? authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("GUEST") : "GUEST";  // Если не аутентифицирован, то роль GUEST

        model.addAttribute("role", role);

        return "about";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

}
