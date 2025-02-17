package com.restaurant.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_types")
public class MenuType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuTypeId;

    @Column(nullable = false, unique = true)
    private String typeName;

    // Геттеры и сеттеры
    public Long getMenuTypeId() {
        return menuTypeId;
    }

    public void setMenuTypeId(Long menuTypeId) {
        this.menuTypeId = menuTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
