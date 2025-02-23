package com.restaurant.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "all_items")
public class AllItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false, unique = true, length = 100)
    private String itemName;

    @Column(nullable = false, length = 50)
    private String productType; // Теперь это просто строка

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
