package com.restaurant.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "access_types")
public class AccessType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accessTypeId;

    @Column(nullable = false, unique = true)
    private String accessName;

    private String description;

    // Геттеры и сеттеры
    public Long getAccessTypeId() {
        return accessTypeId;
    }

    public void setAccessTypeId(Long accessTypeId) {
        this.accessTypeId = accessTypeId;
    }

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
