package com.restaurant.entities;

import jakarta.persistence.*;

/**
 * Класс, представляющий столы в ресторане.
 * Содержит информацию о столе, включая его уникальный идентификатор и количество мест.
 */
@Entity
@Table(name = "all_tables")
public class AllTables {

    /**
     * Уникальный идентификатор стола.
     * Этот идентификатор задается вручную в базе данных, а не генерируется автоматически.
     */
    @Id
    // У нас нет автоинкремента, поэтому убираем @GeneratedValue
    // потому что в SQL эта колонка задается вручную (0, 1, 2, ...).
    private Integer id;

    /**
     * Количество мест на столе.
     * Это поле не может быть пустым.
     */
    @Column(nullable = false)
    private Integer seats;

    /**
     * Конструктор без параметров для JPA.
     * Используется при создании экземпляров класса через рефлексию.
     */
    public AllTables() {
    }

    /**
     * Конструктор с параметрами для создания стола с заданными значениями.
     *
     * @param id    Уникальный идентификатор стола.
     * @param seats Количество мест на столе.
     */
    public AllTables(Integer id, Integer seats) {
        this.id = id;
        this.seats = seats;
    }

    /**
     * Возвращает уникальный идентификатор стола.
     *
     * @return Идентификатор стола.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор стола.
     *
     * @param id Идентификатор стола.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Возвращает количество мест на столе.
     *
     * @return Количество мест на столе.
     */
    public Integer getSeats() {
        return seats;
    }

    /**
     * Устанавливает количество мест на столе.
     *
     * @param seats Количество мест на столе.
     */
    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
