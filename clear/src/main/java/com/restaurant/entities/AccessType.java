package com.restaurant.entities;

import jakarta.persistence.*;

/**
 * Класс, представляющий типы доступа в системе.
 * Содержит информацию о типе доступа, его названии и описании.
 */
@Entity
@Table(name = "access_types")
public class AccessType {

    /**
     * Уникальный идентификатор типа доступа.
     * Этот идентификатор генерируется автоматически в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accessTypeId;

    /**
     * Название типа доступа.
     * Это поле не может быть пустым и должно быть уникальным в базе данных.
     */
    @Column(nullable = false, unique = true)
    private String accessName;

    /**
     * Описание типа доступа.
     * Это поле может быть пустым.
     */
    private String description;

    /**
     * Возвращает уникальный идентификатор типа доступа.
     *
     * @return Идентификатор типа доступа.
     */
    public Long getAccessTypeId() {
        return accessTypeId;
    }

    /**
     * Устанавливает уникальный идентификатор типа доступа.
     *
     * @param accessTypeId Идентификатор типа доступа.
     */
    public void setAccessTypeId(Long accessTypeId) {
        this.accessTypeId = accessTypeId;
    }

    /**
     * Возвращает название типа доступа.
     *
     * @return Название типа доступа.
     */
    public String getAccessName() {
        return accessName;
    }

    /**
     * Устанавливает название типа доступа.
     *
     * @param accessName Название типа доступа.
     */
    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }

    /**
     * Возвращает описание типа доступа.
     *
     * @return Описание типа доступа, может быть {@code null}.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание типа доступа.
     *
     * @param description Описание типа доступа.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
