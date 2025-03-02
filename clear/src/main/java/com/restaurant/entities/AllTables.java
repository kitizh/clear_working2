package com.restaurant.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "all_tables")
public class AllTables {

    @Id
    // У нас нет автоинкремента, поэтому убираем @GeneratedValue
    // потому что в SQL эта колонка задаётся вручную (0, 1, 2, ...).
    private Integer id;

    @Column(nullable = false)
    private Integer seats;

    public AllTables() {
    }

    public AllTables(Integer id, Integer seats) {
        this.id = id;
        this.seats = seats;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
