package com.restaurant.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "reserved_tables")
public class ReservedTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private AllTables table;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public AllTables getTable() {
        return table;
    }

    public void setTable(AllTables table) {
        this.table = table;
    }
}
