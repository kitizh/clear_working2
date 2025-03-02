package com.restaurant.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    // В БД это TIMESTAMP с DEFAULT CURRENT_TIMESTAMP
    // Для хранения в Java можно использовать LocalDateTime или Date.
    @Column(name = "order_time")
    private LocalDateTime orderTime;

    // Связь с таблицей all_tables по столбцу table_number.
    // "ON DELETE SET NULL" означает, что если столик удалят, в orders будет null.
    @ManyToOne
    @JoinColumn(name = "table_number")
    private AllTables allTables;

    private Double totalAmount;
    private String status;
    private String description;

    public Orders() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public AllTables getAllTables() {
        return allTables;
    }

    public void setAllTables(AllTables allTables) {
        this.allTables = allTables;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
