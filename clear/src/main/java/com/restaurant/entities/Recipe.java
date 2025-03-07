package com.restaurant.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    // Изменённая связь: теперь Recipe связывается напрямую с AllItems через столбец item_id
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private AllItems item;

    @Column(nullable = false)
    private Double amount;

    public Long getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
    public Menu getMenu() {
        return menu;
    }
    public void setMenu(Menu menu) {
        this.menu = menu;
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

}
