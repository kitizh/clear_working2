package com.restaurant.repositories;

import com.restaurant.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link User}.
 * Содержит методы для работы с пользователями в системе.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username Имя пользователя.
     * @return Объект {@link Optional}, который может содержать пользователя, если он найден.
     */
    Optional<User> findByUsername(String username);
}
