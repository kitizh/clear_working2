package com.restaurant.controllers;

import com.restaurant.entities.AccessType;
import com.restaurant.entities.User;
import com.restaurant.repositories.AccessTypeRepository;
import com.restaurant.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final AccessTypeRepository accessTypeRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, AccessTypeRepository accessTypeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accessTypeRepository = accessTypeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String getMenuPage() {
        return "index";
    }

    @GetMapping("/about")
    public String getAboutPage() {
        return "about";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/admin/create-user")
    public String createUserForm(Model model) {
        model.addAttribute("accessTypes", accessTypeRepository.findAll());
        return "admin-create-user";
    }

    @PostMapping("/admin/create-user")
    public String createUser(@RequestParam String username, @RequestParam String password, @RequestParam String accessType) {
        AccessType type = accessTypeRepository.findByAccessName(accessType);
        if (type != null) {
            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setAccessType(type);
            userRepository.save(user);
        }
        return "redirect:/admin/create-user?success";
    }
}
