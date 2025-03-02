package com.restaurant.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    // Связь с таблицей orders (поле order_id)
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    // Связь с таблицей menu (поле menu_id)
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    // Комментарий к изменению блюда
    private String description;

    public OrderItem() {
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
