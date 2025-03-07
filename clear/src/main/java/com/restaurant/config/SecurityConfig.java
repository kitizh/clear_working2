package com.restaurant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасности для Spring Security.
 * Настройки безопасности включают в себя конфигурацию для аутентификации пользователей, настройки для логина,
 * а также определения доступа к статическим ресурсам и страницам.
 */
@Configuration
public class SecurityConfig {

    /**
     * Настройка цепочки фильтров безопасности для обработки запросов.
     * Включает в себя настройки CSRF, авторизации для различных URL, а также страницы логина и логаута.
     *
     * @param http {@link HttpSecurity} для настройки безопасности веб-приложения.
     * @return Конфигурированная {@link SecurityFilterChain} для безопасности.
     * @throws Exception Если возникает ошибка при конфигурации безопасности.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Отключение CSRF-защиты для упрощенной работы (можно включить при необходимости)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/images/**", "/js/**", "/menu", "/about/**").permitAll()  // Доступ к статическим ресурсам без авторизации
                        .anyRequest().authenticated()  // Для всех остальных запросов требуется аутентификация
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Страница логина
                        .defaultSuccessUrl("/", false)  // Страница перенаправления после успешного входа
                        .permitAll()  // Разрешить доступ к странице логина всем
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL для выхода
                        .logoutSuccessUrl("/")  // Страница перенаправления после выхода
                        .permitAll()  // Разрешить доступ ко всем пользователям
                );

        return http.build();
    }

    /**
     * Создание {@link AuthenticationManager} для аутентификации пользователей.
     *
     * @param authenticationConfiguration {@link AuthenticationConfiguration} для получения аутентификационного менеджера.
     * @return {@link AuthenticationManager} для управления аутентификацией.
     * @throws Exception Если возникает ошибка при создании аутентификационного менеджера.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Создание {@link PasswordEncoder} для безопасного шифрования паролей.
     * Используется алгоритм BCrypt для хеширования паролей.
     *
     * @return {@link PasswordEncoder} для шифрования паролей.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Используется алгоритм BCrypt для безопасного шифрования
    }
}
