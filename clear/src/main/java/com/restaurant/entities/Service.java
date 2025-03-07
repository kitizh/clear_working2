package com.restaurant.entities;

import jakarta.persistence.*;

/**
 * Класс, представляющий услугу, доступную в ресторане.
 * Каждая услуга может быть забронирована в рамках определенной резервации.
 */
@Entity
@Table(name = "services")
public class Service {

    /**
     * Уникальный идентификатор услуги.
     * Этот идентификатор генерируется автоматически в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название услуги.
     * Это обязательное поле, которое определяет название услуги.
     */
    @Column(name = "service_name", nullable = false)
    private String serviceName;

    /**
     * Описание услуги.
     * Это дополнительное поле, которое может содержать подробности о предоставляемой услуге.
     */
    @Column(name = "description")
    private String description;

    // Геттеры и сеттеры

    /**
     * Возвращает уникальный идентификатор услуги.
     *
     * @return Идентификатор услуги.
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор услуги.
     *
     * @param id Идентификатор услуги.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает название услуги.
     *
     * @return Название услуги.
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Устанавливает название услуги.
     *
     * @param serviceName Название услуги.
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * Возвращает описание услуги.
     *
     * @return Описание услуги.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание услуги.
     *
     * @param description Описание услуги.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
