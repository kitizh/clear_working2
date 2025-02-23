package com.restaurant.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private AllItems item; // Связь с таблицей `all_items`

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false, length = 20)
    private String unit; // Строковое поле

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public AllItems getItem() {
        return item;
    }

    public void setItem(AllItems item) {
        this.item = item;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
