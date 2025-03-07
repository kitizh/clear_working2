package com.restaurant.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Класс, представляющий заказ в ресторане.
 * Содержит информацию о заказе, включая дату, время, стол, общую сумму, статус и описание.
 */
@Entity
@Table(name = "orders")
public class Orders {

    /**
     * Уникальный идентификатор заказа.
     * Этот идентификатор генерируется автоматически в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    /**
     * Дата заказа.
     * Значение по умолчанию: текущая дата.
     */
    @Column(nullable = false)
    private LocalDate orderDate;

    /**
     * Время заказа.
     * Значение по умолчанию: текущее время.
     */
    @Column(nullable = false)
    private LocalTime orderTime;

    /**
     * Связь с таблицей {@link AllTables}.
     * Это поле представляет собой стол, за которым был сделан заказ.
     * Может быть {@code null}, если стол не был выбран.
     */
    @ManyToOne
    @JoinColumn(name = "table_number", referencedColumnName = "id", nullable = true)
    private AllTables allTables;

    /**
     * Общая сумма заказа.
     * Значение по умолчанию: 0.0.
     */
    @Column(nullable = false)
    private Double totalAmount;

    /**
     * Статус заказа.
     * Значение по умолчанию: "Принят".
     */
    @Column(nullable = false)
    private String status;

    /**
     * Описание заказа.
     * Значение по умолчанию: "-".
     */
    @Column(nullable = false)
    private String description;

    /**
     * Конструктор с дефолтными значениями.
     * Инициализирует поля с текущими значениями:
     * - дата заказа: текущая дата
     * - время заказа: текущее время
     * - общая сумма: 0.0
     * - статус: "Принят"
     * - описание: "-"
     * - стол: null
     */
    public Orders() {
        this.orderDate = LocalDate.now();  // Значение по умолчанию: текущая дата
        this.orderTime = LocalTime.now();  // Значение по умолчанию: текущее время
        this.totalAmount = 0.0;           // Значение по умолчанию: 0
        this.status = "Принят";           // Значение по умолчанию: "Принят"
        this.description = "-";           // Значение по умолчанию: "-"
        this.allTables = null;            // Можно оставить null, если столик не задан
    }

    /**
     * Возвращает уникальный идентификатор заказа.
     *
     * @return Идентификатор заказа.
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * Устанавливает уникальный идентификатор заказа.
     *
     * @param orderId Идентификатор заказа.
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * Возвращает дату заказа.
     *
     * @return Дата заказа.
     */
    public LocalDate getOrderDate() {
        return orderDate;
    }

    /**
     * Устанавливает дату заказа.
     *
     * @param orderDate Дата заказа.
     */
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Возвращает время заказа.
     *
     * @return Время заказа.
     */
    public LocalTime getOrderTime() {
        return orderTime;
    }

    /**
     * Устанавливает время заказа.
     *
     * @param orderTime Время заказа.
     */
    public void setOrderTime(LocalTime orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * Возвращает стол, за которым был сделан заказ.
     *
     * @return Стол, за которым был сделан заказ, может быть {@code null}, если стол не был выбран.
     */
    public AllTables getAllTables() {
        return allTables;
    }

    /**
     * Устанавливает стол для заказа.
     *
     * @param allTables Стол, за которым был сделан заказ.
     */
    public void setAllTables(AllTables allTables) {
        this.allTables = allTables;
    }

    /**
     * Возвращает общую сумму заказа.
     *
     * @return Общая сумма заказа.
     */
    public Double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Устанавливает общую сумму заказа.
     *
     * @param totalAmount Общая сумма заказа.
     */
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Возвращает статус заказа.
     *
     * @return Статус заказа (например, "Принят", "Обработан", "Завершен").
     */
    public String getStatus() {
        return status;
    }

    /**
     * Устанавливает статус заказа.
     *
     * @param status Статус заказа.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Возвращает описание заказа.
     *
     * @return Описание заказа.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание заказа.
     *
     * @param description Описание заказа.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
