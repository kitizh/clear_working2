package com.restaurant.entities;

import jakarta.persistence.*;

/**
 * Класс, представляющий пользователя системы, например, администратора или сотрудника ресторана.
 * Каждый пользователь имеет уникальное имя и пароль, а также определенный уровень доступа.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Уникальный идентификатор пользователя.
     * Этот идентификатор генерируется автоматически в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    /**
     * Имя пользователя.
     * Это обязательное поле, которое должно быть уникальным для каждого пользователя.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Хэш пароля пользователя.
     * Это обязательное поле, которое хранит хэш пароля для безопасности.
     */
    @Column(nullable = false)
    private String passwordHash;

    /**
     * Связь с таблицей {@link AccessType}.
     * Это поле представляет собой тип доступа пользователя, например, администратор или сотрудник.
     */
    @ManyToOne
    @JoinColumn(name = "access_type_id", nullable = false)
    private AccessType accessType;

    // Геттеры и сеттеры

    /**
     * Возвращает уникальный идентификатор пользователя.
     *
     * @return Идентификатор пользователя.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Устанавливает уникальный идентификатор пользователя.
     *
     * @param userId Идентификатор пользователя.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Возвращает имя пользователя.
     *
     * @return Имя пользователя.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Устанавливает имя пользователя.
     *
     * @param username Имя пользователя.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Возвращает хэш пароля пользователя.
     *
     * @return Хэш пароля пользователя.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Устанавливает хэш пароля пользователя.
     *
     * @param passwordHash Хэш пароля пользователя.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Возвращает тип доступа пользователя.
     *
     * @return Тип доступа пользователя.
     */
    public AccessType getAccessType() {
        return accessType;
    }

    /**
     * Устанавливает тип доступа пользователя.
     *
     * @param accessType Тип доступа пользователя.
     */
    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }
}
