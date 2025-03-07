package com.restaurant.entities;

import jakarta.persistence.*;

/**
 * Класс, представляющий складской учёт для предметов, используемых в ресторане.
 * Каждая запись в этой таблице связывает определенный товар (предмет) с его количеством на складе.
 */
@Entity
@Table(name = "stock")
public class Stock {

    /**
     * Уникальный идентификатор записи на складе.
     * Этот идентификатор генерируется автоматически в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    /**
     * Связь с таблицей {@link AllItems}.
     * Это поле представляет собой товар (предмет), который хранится на складе.
     */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private AllItems item;

    /**
     * Количество товара на складе.
     * Это обязательное поле, которое хранит информацию о количестве данного товара.
     */
    @Column(nullable = false)
    private Double amount;

    // Геттеры и сеттеры

    /**
     * Возвращает уникальный идентификатор записи на складе.
     *
     * @return Идентификатор записи на складе.
     */
    public Long getStockId() {
        return stockId;
    }

    /**
     * Устанавливает уникальный идентификатор записи на складе.
     *
     * @param stockId Идентификатор записи на складе.
     */
    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    /**
     * Возвращает товар (предмет), который хранится на складе.
     *
     * @return Товар на складе.
     */
    public AllItems getItem() {
        return item;
    }

    /**
     * Устанавливает товар (предмет), который хранится на складе.
     *
     * @param item Товар, который хранится на складе.
     */
    public void setItem(AllItems item) {
        this.item = item;
    }

    /**
     * Возвращает количество товара на складе.
     *
     * @return Количество товара на складе.
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Устанавливает количество товара на складе.
     *
     * @param amount Количество товара на складе.
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
