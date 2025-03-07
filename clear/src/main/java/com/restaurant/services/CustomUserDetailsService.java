package com.restaurant.services;

import com.restaurant.entities.User;
import com.restaurant.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис, реализующий {@link UserDetailsService} для работы с пользовательскими данными в контексте Spring Security.
 * Используется для загрузки пользователя по имени пользователя для аутентификации и авторизации.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Конструктор для инъекции зависимостей.
     *
     * @param userRepository Репозиторий для работы с данными пользователя.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Загружает пользователя по имени пользователя для аутентификации.
     *
     * @param username Имя пользователя.
     * @return {@link UserDetails} объект для аутентификации пользователя.
     * @throws UsernameNotFoundException Если пользователь с указанным именем не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Поиск пользователя в базе данных по имени
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Возвращаем объект UserDetails для Spring Security с данными пользователя
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .roles(user.getAccessType().getAccessName())  // Роли получаем от типа доступа
                .build();
    }
}
