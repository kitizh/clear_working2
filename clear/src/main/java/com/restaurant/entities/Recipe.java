package com.restaurant.entities;

import jakarta.persistence.*;

/**
 * Класс, представляющий рецепт для блюда в меню.
 * Содержит информацию о связи блюда с его ингредиентами (элементами) и их количестве.
 */
@Entity
@Table(name = "recipe")
public class Recipe {

    /**
     * Уникальный идентификатор рецепта.
     * Этот идентификатор генерируется автоматически в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    /**
     * Связь с таблицей {@link Menu}.
     * Это поле представляет собой блюдо из меню, для которого создан рецепт.
     * Рецепт может содержать несколько ингредиентов.
     */
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    /**
     * Связь с таблицей {@link AllItems}.
     * Это поле представляет собой ингредиент блюда, который используется в рецепте.
     */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private AllItems item;

    /**
     * Количество ингредиента, необходимое для блюда.
     * Это поле не может быть пустым.
     */
    @Column(nullable = false)
    private Double amount;

    /**
     * Возвращает уникальный идентификатор рецепта.
     *
     * @return Идентификатор рецепта.
     */
    public Long getRecipeId() {
        return recipeId;
    }

    /**
     * Устанавливает уникальный идентификатор рецепта.
     *
     * @param recipeId Идентификатор рецепта.
     */
    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    /**
     * Возвращает блюдо из меню, для которого создан рецепт.
     *
     * @return Блюдо из меню.
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * Устанавливает блюдо из меню, для которого создается рецепт.
     *
     * @param menu Блюдо из меню.
     */
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    /**
     * Возвращает ингредиент, используемый в рецепте.
     *
     * @return Ингредиент блюда.
     */
    public AllItems getItem() {
        return item;
    }

    /**
     * Устанавливает ингредиент, используемый в рецепте.
     *
     * @param item Ингредиент блюда.
     */
    public void setItem(AllItems item) {
        this.item = item;
    }

    /**
     * Возвращает количество ингредиента, необходимое для блюда.
     *
     * @return Количество ингредиента.
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Устанавливает количество ингредиента, необходимое для блюда.
     *
     * @param amount Количество ингредиента.
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
