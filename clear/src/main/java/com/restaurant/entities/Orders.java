package com.restaurant.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDate orderDate;

    @Column(nullable = false, columnDefinition = "TIME DEFAULT CURRENT_TIME")
    private LocalTime orderTime;

    @ManyToOne
    @JoinColumn(name = "table_number", referencedColumnName = "id", nullable = true)
    private AllTables allTables;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Double totalAmount;

    @Column(nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'Принят'")
    private String status;

    @Column(nullable = false, columnDefinition = "TEXT DEFAULT '-'")
    private String description;

    // Конструктор с дефолтными значениями
    public Orders() {
        this.orderDate = LocalDate.now();  // Значение по умолчанию: текущая дата
        this.orderTime = LocalTime.now();  // Значение по умолчанию: текущее время
        this.totalAmount = 0.0;           // Значение по умолчанию: 0
        this.status = "Принят";           // Значение по умолчанию: "Принят"
        this.description = "-";           // Значение по умолчанию: "-"
        this.allTables = null;            // Можно оставить null, если столик не задан
    }

    // Геттеры и сеттеры
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalTime orderTime) {
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
