package com.restaurant.entities;

import jakarta.persistence.*;

/**
 * Класс, представляющий элемент заказа в ресторане.
 * Содержит информацию о блюде, заказе и дополнительном описании для элемента.
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    /**
     * Уникальный идентификатор элемента заказа.
     * Этот идентификатор генерируется автоматически в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    /**
     * Связь с таблицей {@link Orders}.
     * Это поле представляет собой заказ, к которому относится данный элемент.
     */
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    /**
     * Связь с таблицей {@link Menu}.
     * Это поле представляет собой блюдо, которое было заказано.
     */
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    /**
     * Описание изменения блюда.
     * Это поле может быть использовано для указания особых пожеланий или изменений, связанных с заказом.
     */
    private String description;

    /**
     * Конструктор без параметров для JPA.
     * Используется при создании экземпляров класса через рефлексию.
     */
    public OrderItem() {
    }

    /**
     * Возвращает уникальный идентификатор элемента заказа.
     *
     * @return Идентификатор элемента заказа.
     */
    public Long getOrderItemId() {
        return orderItemId;
    }

    /**
     * Устанавливает уникальный идентификатор элемента заказа.
     *
     * @param orderItemId Идентификатор элемента заказа.
     */
    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    /**
     * Возвращает заказ, к которому относится данный элемент.
     *
     * @return Заказ, к которому относится элемент.
     */
    public Orders getOrder() {
        return order;
    }

    /**
     * Устанавливает заказ, к которому относится данный элемент.
     *
     * @param order Заказ, к которому относится элемент.
     */
    public void setOrder(Orders order) {
        this.order = order;
    }

    /**
     * Возвращает блюдо, которое было заказано.
     *
     * @return Блюдо, связанное с элементом заказа.
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * Устанавливает блюдо, которое было заказано.
     *
     * @param menu Блюдо, которое было заказано.
     */
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    /**
     * Возвращает описание изменения блюда.
     *
     * @return Описание изменений или пожеланий относительно блюда.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание изменения блюда.
     *
     * @param description Описание изменения или пожеланий для блюда.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
