package com.restaurant.entities;

import jakarta.persistence.*;

/**
 * Класс, представляющий меню ресторана.
 * Содержит информацию о блюде, его названии, описании, цене и типе меню.
 */
@Entity
@Table(name = "menu")
public class Menu {

    /**
     * Уникальный идентификатор блюда в меню.
     * Этот идентификатор генерируется автоматически в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    /**
     * Название блюда.
     * Это поле не может быть пустым.
     */
    @Column(nullable = false)
    private String dishName;

    /**
     * Описание блюда.
     * Это поле может быть пустым.
     */
    private String description;

    /**
     * Цена блюда.
     * Это поле не может быть пустым.
     */
    @Column(nullable = false)
    private Double price;

    /**
     * Тип меню, к которому относится блюдо (например, завтрак, ужин, десерт и т.д.).
     * Это поле не может быть пустым.
     */
    @Column(nullable = false)
    private String menuType;

    /**
     * Возвращает уникальный идентификатор блюда в меню.
     *
     * @return Идентификатор блюда в меню.
     */
    public Long getMenuId() {
        return menuId;
    }

    /**
     * Устанавливает уникальный идентификатор блюда в меню.
     *
     * @param menuId Идентификатор блюда в меню.
     */
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    /**
     * Возвращает название блюда.
     *
     * @return Название блюда.
     */
    public String getDishName() {
        return dishName;
    }

    /**
     * Устанавливает название блюда.
     *
     * @param dishName Название блюда.
     */
    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    /**
     * Возвращает описание блюда.
     *
     * @return Описание блюда, может быть {@code null}.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание блюда.
     *
     * @param description Описание блюда.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Возвращает цену блюда.
     *
     * @return Цена блюда.
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Устанавливает цену блюда.
     *
     * @param price Цена блюда.
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Возвращает тип меню, к которому относится блюдо.
     *
     * @return Тип меню (например, завтрак, ужин, десерт и т.д.).
     */
    public String getMenuType() {
        return menuType;
    }

    /**
     * Устанавливает тип меню, к которому относится блюдо.
     *
     * @param menuType Тип меню.
     */
    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
}
