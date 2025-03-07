package com.restaurant.entities;

import jakarta.persistence.*;

/**
 * Класс, представляющий товар в ресторане.
 * Содержит информацию о товаре, включая его уникальный идентификатор, название и тип продукта.
 */
@Entity
@Table(name = "all_items")
public class AllItems {

    /**
     * Уникальный идентификатор товара.
     * Этот идентификатор генерируется автоматически в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    /**
     * Название товара.
     * Это поле не может быть пустым и должно быть уникальным в базе данных.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String itemName;

    /**
     * Тип продукта, к которому относится товар.
     * Это поле не может быть пустым и ограничено длиной в 50 символов.
     * Теперь это просто строка, без дополнительных структур.
     */
    @Column(nullable = false, length = 50)
    private String productType;

    /**
     * Возвращает уникальный идентификатор товара.
     *
     * @return Идентификатор товара.
     */
    public Long getItemId() {
        return itemId;
    }

    /**
     * Устанавливает уникальный идентификатор товара.
     *
     * @param itemId Идентификатор товара.
     */
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    /**
     * Возвращает название товара.
     *
     * @return Название товара.
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Устанавливает название товара.
     *
     * @param itemName Название товара.
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Возвращает тип продукта товара.
     *
     * @return Тип продукта товара.
     */
    public String getProductType() {
        return productType;
    }

    /**
     * Устанавливает тип продукта товара.
     *
     * @param productType Тип продукта товара.
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }
}
